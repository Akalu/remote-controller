package remote.ct.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import remote.ct.model.AutomationProcess;
import remote.ct.model.Node;
import remote.ct.model.Status;
import remote.ct.services.KeyService;
import remote.ct.services.QueueService;
import remote.ct.ssh.Key;

public class Demo2 {
	
	static final String PATH = "/application.properties";
	static final QueueService service = new QueueService();

	public static void main(String[] arg) throws Exception {
		demonstration(service);
	}
	
	public static Properties loadConfig(String path) throws Exception {
		Properties props = new Properties();
		try (InputStream is = Demo2.class.getResourceAsStream(path)) {
			props.load(is);
		} catch (IOException e) {
			throw new Exception("Failed to init load starter configugation", e);
		}
		return props;
	}
	
	public static void demonstration(QueueService service) throws Exception {
		
		
		Properties prop = loadConfig(PATH);
		Key key = new Key(prop.getProperty("node.username"), prop.getProperty("node.password"));
		KeyService.getInstance().put("test", key);

		// Create producer thread
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					service.generate();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});

		// Create consumer thread
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
					try {
						service.process();
					} catch (ExecutionException | InterruptedException e) {
						e.printStackTrace();
					}
			}
		});

		// Start both threads
		t1.start();
		t2.start();

		Node node = new Node();
		node.setCreds("test");
		node.setName("test_node_1");
		node.setHost("localhost");
		node.setPort(22);
		node.setStatus(Status.IDLE);
		service.add(node);
		
		boolean exit = true;
		int idx = 0;
		while (exit) {
			Thread.sleep(3000);
			
			AutomationProcess bp = new AutomationProcess();
			bp.setName("test "+idx++);
			bp.setRunScriptName("java -version");
			bp.setBpId(UUID.randomUUID().toString());
			if (idx < 5)
			service.add(bp);
		}
		
		t1.join();
		t2.join();


	}

}
