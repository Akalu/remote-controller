package remote.ct.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskService {
	private static final int THREADS_COUNT = 10;

	private static volatile TaskService instance = null;
	
	ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);

	private TaskService() {
	}

	public static TaskService getInstance() {
		if (instance == null) {
			instance = new TaskService();
		}
		return instance;
	}
	
	public void shutdown() {
		executor.shutdown();
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}


}
