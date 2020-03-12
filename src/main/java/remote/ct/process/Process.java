package remote.ct.process;

import remote.ct.ssh.SshConnection;

public interface Process {
	void execute(SshConnection sshConnection);
	String getLog();
}
