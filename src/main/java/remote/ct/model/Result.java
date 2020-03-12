package remote.ct.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Result {
	public static final Gson gson = new GsonBuilder().create();
	private String bpId;
	private String runId;
	private String log;
	private String exception;
	
	public Result() {
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getException() {
		return exception;
	}

	public void setException(String e) {
		this.exception = e;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	@Override
	public String toString() {
		return gson.toJson(this);
	}
	
	
}
