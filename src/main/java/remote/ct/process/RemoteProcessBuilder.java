package remote.ct.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import remote.ct.model.Node;
import remote.ct.services.KeyService;
import remote.ct.ssh.Key;
import remote.ct.ssh.SshConnection;
import remote.ct.ssh.SshConnectionFactory;

/**
 * 	Designed as an analog of the standard class ProcessBuilder
 */
public class RemoteProcessBuilder {
	private List<Process> processes;

	public RemoteProcessBuilder() {
		processes = new ArrayList<>();
	}

	public void add(Process process) {
		processes.add(process);
	}

	public void execOn(Node node) throws Exception {
		SshConnectionFactory factory = SshConnectionFactory.getInstance();
		Optional<Key> key = KeyService.getInstance().get(node.getCreds());
		if (!key.isPresent()) {
			throw new IllegalArgumentException("Cannot find credentials to node "+node.getName());
		}
		for (Process process : processes) {
			try (SshConnection conn = factory.getConnection(node, key.get())) {
				process.execute(conn);
			}
		}
	}
	
	public String getLogs() {
		StringBuilder sb = new StringBuilder();
		for (Process process : processes) {
			sb.append(process.getLog());
		}
		return sb.toString();
	}
}
