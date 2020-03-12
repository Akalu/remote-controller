package remote.ct.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import remote.ct.ssh.SshConnection;

public class ProcessMvnCommand implements Process {

	private static final Logger logger = LoggerFactory.getLogger(ProcessMvnCommand.class);
	private String suffix;
	private StringBuilder log = new StringBuilder();
	
	public ProcessMvnCommand() {
		suffix = "--version";
	}

	public ProcessMvnCommand(String[] args) {
		suffix = String.join(" ", args);
	}

	@Override
	public void execute(SshConnection sshConnection) {
		
		String execLine1 = "mvn " + suffix + "\n";
		try {
			sshConnection.open();
			String result1 = sshConnection.executeCommand(execLine1);
			logger.debug(result1);
			logger.debug("[" + result1.length() + " bytes]");
			log.append(result1);
		} catch (Exception e) {
			e.printStackTrace();
			log.append(e.getMessage());
		}
		
	}
	
	public String getLog() {
		return log.toString();
	}

}
