package remote.ct.ssh;

import remote.ct.model.Node;

public class SshConnectionFactory {

	public static int TIMEOUT = 30000;

	private static volatile SshConnectionFactory instance = null;

	private SshConnectionFactory() {
	}

	public static SshConnectionFactory getInstance() {
		if (instance == null) {
			instance = new SshConnectionFactory();
		}
		return instance;
	}


	public SshConnection getConnection(Node node, Key key) {
		return new SshConnection(node, key, TIMEOUT);
	}

}
