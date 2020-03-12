package remote.ct.model;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import remote.ct.services.TaskService;

public abstract class Task implements Callable<Result> {
	
	protected AutomationProcess businessProcess;
	protected Future<Result> future;
	protected long timeLimit = 0;

	public String submit() {
		future = TaskService.getInstance().getExecutor().submit(this);
		UUID runId = UUID.randomUUID();
		businessProcess.setRunId(runId.toString());
		Timestamp started = new Timestamp(System.currentTimeMillis());
		businessProcess.setStarted(started);
		return runId.toString();
	}

	public Future<Result> getFuture() {
		return future;
	}

	public AutomationProcess getBusinessProcess() {
		return businessProcess;
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public boolean isExpired() {
		if (timeLimit != 0) {
			return businessProcess.getStarted().getTime() + timeLimit > System.currentTimeMillis();
		}
		return true;
	}
}
