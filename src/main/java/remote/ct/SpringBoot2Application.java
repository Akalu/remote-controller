package remote.ct;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import remote.ct.dao.BusinessProcessRepository;
import remote.ct.dao.impl.JdbcDataTORepository;
import remote.ct.dao.impl.JdbcNodeRepository;
import remote.ct.demo.Demo2;
import remote.ct.model.AutomationProcess;
import remote.ct.model.DataTO;
import remote.ct.model.Node;
import remote.ct.model.ProcessStatus;
import remote.ct.model.SearchCriteria;
import remote.ct.model.Status;
import remote.ct.services.QueueService;


@SpringBootApplication
public class SpringBoot2Application {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringBoot2Application.class);
	
	@Autowired
	@Qualifier("JdbcBusinessProcessRepository")
	private BusinessProcessRepository businessProcessRepository;

	@Autowired
	@Qualifier("JdbcNodeRepository")
	private JdbcNodeRepository nodeRepository;

	@Autowired
	private JdbcDataTORepository dataRepository;
	
	@Autowired
	private QueueService queueService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot2Application.class, args);
	}
	
	@Bean
	CommandLineRunner runner() {
		return args -> {
			logger.info("=================== queueService:"+queueService);
			
			Demo2.demonstration(queueService);
			
			AutomationProcess bp = new AutomationProcess();
			bp.setName("test 1");
			bp.setRunScriptName("java -version");
			String bpId = UUID.randomUUID().toString();
			bp.setBpId(bpId);
			logger.info("to save:"+bp);
			int res = businessProcessRepository.upsert("processes", bp);
			logger.info("saved:"+res);
			AutomationProcess found = businessProcessRepository.findById("processes", bpId);
			logger.info("found:"+found);
			bp.setProcessStatus(ProcessStatus.QUEUED);
			int resOfUpd = businessProcessRepository.upsert("processes", bp);
			logger.info("upd:"+resOfUpd);
			List<AutomationProcess> all = businessProcessRepository.getAll("processes");
			logger.info("all:"+all);
			
			Node node = new Node();
			node.setCreds("test");
			node.setName("test node 1");
			node.setHost("localhost");
			node.setPort(22);
			node.setStatus(Status.IDLE);
			logger.info("to save:"+bp);
			int res1 = nodeRepository.upsert("nodes", node);
			logger.info("saved:"+res1);
			List<Node> allNodes = nodeRepository.getAll("nodes");
			logger.info("all nodes "+allNodes);
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setCriteria("status='IDLE'");
			Node foundNode = nodeRepository.getAndLock("nodes", searchCriteria);
			logger.info("foundNode:"+foundNode);
			foundNode.setStatus(Status.BUSY);
			int updRes = nodeRepository.upsert("nodes", foundNode);
			logger.info("node update result:"+updRes);
			int unlockRes = nodeRepository.unlock("nodes", "test node 1");
			logger.info("node unlock result:"+unlockRes);
			
			DataTO rec = new  DataTO();
			rec.setId(bp.getBpId());
			rec.setOwner("1");
			rec.setData(bp.toString());
			dataRepository.upsert("node_audit", bp.getBpId(), rec);
			logger.info("node update result:"+updRes);
			
		};
	}
	

}