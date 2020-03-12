package remote.ct.model;

import remote.ct.process.ProcessShellCommand;
import remote.ct.process.RemoteProcessBuilder;

public class StopTask extends Task {
	
	public StopTask(AutomationProcess businessProcess) {
		this.businessProcess = businessProcess;
	}

	@Override
	public Result call() throws Exception {
		businessProcess.setProcessStatus(ProcessStatus.STOPPING);
		Result result = new Result();
		result.setRunId(businessProcess.getRunId());
		ProcessShellCommand cmd = new ProcessShellCommand(businessProcess.getStopScriptName());
		RemoteProcessBuilder pb = new RemoteProcessBuilder();
		pb.add(cmd);
		pb.execOn(businessProcess.getNode());
		result.setLog(pb.getLogs());
		businessProcess.setProcessStatus(ProcessStatus.STOPPED);
		
		return result;
	}
	
}
