package remote.ct.ssh;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import remote.ct.logger.StreamLogger;
import remote.ct.model.Node;

public class SshConnection implements AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(SshConnection.class);

	// Constants
	private static final String STRICT_HOSTKEY_CHECKIN_KEY = "StrictHostKeyChecking";
	private static final String STRICT_HOSTKEY_CHECKIN_VALUE = "no";
	private static final String CHANNEL_TYPE_SHELL = "shell";
	private static final String CHANNEL_TYPE_EXEC = "exec";

	private Node node;
	private Key key;
	// Connection timeout
	private int timeout;

	private Session session;
	private InputStream input;
	private ChannelExec channel;
	final StreamLogger streamLogger = new StreamLogger();

	/**
	 * Basic constructor
	 * 
	 * @param node , creds
	 * @param key 
	 * @param timeout   , the connection timeout
	 */
	public SshConnection(Node node, Key key, int timeout) {
		this.key = key;
		this.node = node;
		this.timeout = timeout;
	}

	/**
	 * Open a connection
	 * 
	 * @throws JSchException if a error due to the ssh server connection...
	 * @throws IOException
	 * 
	 */
	public void open() throws JSchException, IOException {

		// Prepare session
		final JSch jsch = new JSch();
		
		session = jsch.getSession(key.getLogin(), node.getHost(), node.getPort());
		session.setPassword(key.getPassword());
		session.setTimeout(timeout);
		session.setConfig(STRICT_HOSTKEY_CHECKIN_KEY, STRICT_HOSTKEY_CHECKIN_VALUE);

		// Start a connection
		logger.info(String.format("Try to connect to the server %s:%s with user %s", node.getHost(),
				node.getPort(), key.getLogin()));
		session.connect();
		logger.info("Connection OK");

		logger.info("Open SSH channel");
		channel = (ChannelExec) session.openChannel(CHANNEL_TYPE_EXEC);

		//input = channel.getInputStream();
		channel.getOutputStream();
		// Redirect the output stream to StreamLogger
		channel.setExtOutputStream(streamLogger);
	}

	/**
	 * Execute a command and return the result as a String
	 * 
	 * @param command the command to execute
	 * @return the result as a String
	 * @throws IOException
	 * @throws JSchException
	 */
	public String executeCommand(String command) throws IOException, JSchException {
		if (!session.isConnected()) {
			throw new RuntimeException("Not connected to an open session.  Call open() first!");
		}
		logger.info("Executing: " + command);
		channel.setCommand(command);
		input = channel.getInputStream();
		channel.connect();

		int size = 1024;
		final byte[] tmp = new byte[size];
		final StringBuilder sb = new StringBuilder();

		while (true) {

			while (input.available() > 0) {
				int i = input.read(tmp, 0, 1024);
				if (i < 0) {
					break;
				}
//				sb.append(new String(tmp, 0, i));
			}

			if (channel.isClosed()) {
				if (input.available() > 0) {
					int i = input.read(tmp, 0, 1024);
//					sb.append(new String(tmp, 0, i));
				}
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		channel.disconnect();

		return streamLogger.getLog();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	public void close() throws Exception {
		if (session != null) {
			session.disconnect();
		}

	}
}
