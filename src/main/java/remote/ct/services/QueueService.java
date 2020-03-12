package remote.ct.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import remote.ct.model.AutomationProcess;
import remote.ct.model.Node;
import remote.ct.model.ProcessStatus;
import remote.ct.model.Result;
import remote.ct.model.RunTask;
import remote.ct.model.Task;

/**
 * 	Core functionality
 */
public class QueueService {
	private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
	private int poolInterval = 3000;

	private final ConcurrentMap<String, Task> activeTasks = new ConcurrentHashMap<>();

	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private BusinessProcessService bpService;
	
	@Autowired
	private AuditService auditService;

	public QueueService() {
	}

	synchronized public void add(AutomationProcess businessProcess) throws InterruptedException, ExecutionException {
		logger.info("add:" + businessProcess);
		bpService.add(businessProcess);
	}

	synchronized public void add(Node node) throws InterruptedException, ExecutionException {
		nodeService.addNode(node);
	}

	public void generate() throws InterruptedException, ExecutionException {
		while (true) {
			AutomationProcess businessProcess = bpService.peek();
			while (businessProcess == null) {
				logger.info(" ==== generate wait for fresh businessProcess ==== ");
				businessProcess = bpService.peek();
				Thread.sleep(poolInterval);
			}
			businessProcess.setProcessStatus(ProcessStatus.QUEUED);
			// try to get available node next (after having bp to work on)
			Optional<Node> freeNode = nodeService.getAvailableNode();
			while (!freeNode.isPresent()) {
				logger.info(" ==== generate wait for node ==== ");
				freeNode = nodeService.getAvailableNode();
				Thread.sleep(poolInterval);
			}
			
			logger.info("to exec:" + businessProcess);
			
			businessProcess.setNode(freeNode.get());
			// create a new Task on the basis of BP
			Task runTask = new RunTask(businessProcess);
			String uuid = runTask.submit();
			activeTasks.put(uuid, runTask);
		}
	}

	
	public void process() throws InterruptedException, ExecutionException {
		while (true) {
			logger.info(" ==== process ==== ");
			while (activeTasks.size() == 0) {
				logger.info(" ==== process wait for activeTasks cases ==== ");
				Thread.sleep(poolInterval);
			}
			List<AutomationProcess> toUpdate = new ArrayList<>();
			List<Result> results = new ArrayList<>();
			while (results.isEmpty()) {
				logger.info(" ==== process results ==== ");
				// clean up activeTasks tasks list
				Set<String> keys = activeTasks.keySet();
				for (String runId : keys) {
					Task task = activeTasks.get(runId);
					Future<Result> future = task.getFuture();
					AutomationProcess bp = task.getBusinessProcess();
					// check the task is done
					if (future.isDone()) {
						Timestamp finished = new Timestamp(System.currentTimeMillis());
						bp.setFinished(finished);
						bp.setProcessStatus(ProcessStatus.FINISHED);
						nodeService.returnNode(bp.getNode());//TODO rethink relations BP <-> Node
						toUpdate.add(bp);
						results.add(future.get());
					} else if (future.isCancelled()) {
						Timestamp finished = new Timestamp(System.currentTimeMillis());
						bp.setFinished(finished);
						bp.setProcessStatus(ProcessStatus.STOPPED);
						nodeService.returnNode(bp.getNode());
						toUpdate.add(bp);
						results.add(future.get());
					}
					// check timeout scenario
					if (task.isExpired()) {

					}
				}
				Thread.sleep(poolInterval);
			}
			// process finished
			for (AutomationProcess bp : toUpdate) {
				activeTasks.remove(bp.getRunId());
				bpService.upsert(bp);
				bpService.unlock(bp);
				logger.info("finished:" + bp);
				logger.info("node status:" + bp.getNode());
			}
			//process output
			for (Result r : results) {
				logger.info("Result:" + r);
				auditService.save(r.getBpId(), r);
			}			
			
		}
	}

}
