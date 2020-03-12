package remote.ct.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import remote.ct.ssh.SshConnection;

public class ProcessShellCommand implements Process {

	private static final Logger logger = LoggerFactory.getLogger(ProcessShellCommand.class);
	
	private String cmd;
	private String suffix;
	private StringBuilder log = new StringBuilder();
	
	public ProcessShellCommand(String cmd) {
		this.cmd = cmd;
		suffix = "";
	}

	public ProcessShellCommand(String cmd, String[] args) {
		this.cmd = cmd;
		suffix = String.join(" ", args);
	}


	@Override
	public void execute(SshConnection sshConnection) {
		String execLine2 = cmd + suffix + "\n";
		try {
			sshConnection.open();
			String result2 = sshConnection.executeCommand(execLine2);
			logger.info(result2);
			logger.info("[" + result2.length() + " bytes]");
			log.append(result2);
		} catch (Exception e) {
			e.printStackTrace();
			log.append(e.getMessage());
		}
		
	}
	
	@Override
	public String getLog() {
		return log.toString();
	}

}
