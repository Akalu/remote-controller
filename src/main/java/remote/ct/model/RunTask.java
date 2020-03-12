package remote.ct.model;

import remote.ct.process.ProcessShellCommand;
import remote.ct.process.RemoteProcessBuilder;

public class RunTask extends Task {
	
	public RunTask(AutomationProcess businessProcess) {
		this.businessProcess = businessProcess;
	}

	@Override
	public Result call() throws Exception {
		businessProcess.setProcessStatus(ProcessStatus.RUNNING);
		Result result = new Result();
		result.setBpId(businessProcess.getBpId());
		result.setRunId(businessProcess.getRunId());
		ProcessShellCommand cmd = new ProcessShellCommand(businessProcess.getRunScriptName());
		RemoteProcessBuilder pb = new RemoteProcessBuilder();
		pb.add(cmd);
		pb.execOn(businessProcess.getNode());
		result.setLog(pb.getLogs());
		businessProcess.setProcessStatus(ProcessStatus.FINISHED);
		return result;
	}
	
}
