package remote.ct.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import remote.ct.ssh.SshConnection;

public class ProcessGitCommand implements Process {

	private static final Logger logger = LoggerFactory.getLogger(ProcessGitCommand.class);

	@Override
	public void execute(SshConnection sshConnection) {
		String execLine2 = "git --version\n";
		try {
			sshConnection.open();
			String result2 = sshConnection.executeCommand(execLine2);
			logger.debug(result2);
			logger.debug("[" + result2.length() + " bytes]");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getLog() {
		// TODO Auto-generated method stub
		return null;
	}

}
