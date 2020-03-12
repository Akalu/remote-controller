package remote.ct.model;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class AutomationProcess {

	public static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	public static final Type mapType = new TypeToken<Map<String, String>>() {
	}.getType();

	@Expose
	private String runId;
	@Expose
	private String bpId;
	@Expose
	private String name;
	@Expose
	private String sourceUrl;
	@Expose
	private String buildScriptName;
	@Expose
	private String runScriptName;
	@Expose
	private String stopScriptName;
	@Expose
	private ProcessStatus processStatus;
	@Expose
	private Node node;
	@Expose
	private Timestamp started;// updated only on process' start
	@Expose
	private Timestamp finished;
	@Expose
	private boolean lock = false;
	
	public AutomationProcess() {
		processStatus = ProcessStatus.CREATED;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getBuildScriptName() {
		return buildScriptName;
	}

	public void setBuildScriptName(String buildScriptName) {
		this.buildScriptName = buildScriptName;
	}

	public String getRunScriptName() {
		return runScriptName;
	}

	public void setRunScriptName(String runScriptName) {
		this.runScriptName = runScriptName;
	}

	public ProcessStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(ProcessStatus processStatus) {
		this.processStatus = processStatus;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Timestamp getStarted() {
		return started;
	}

	public void setStarted(Timestamp started) {
		this.started = started;
	}

	public Timestamp getFinished() {
		return finished;
	}

	public void setFinished(Timestamp finished) {
		this.finished = finished;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public String getStopScriptName() {
		return stopScriptName;
	}

	public void setStopScriptName(String stopScriptName) {
		this.stopScriptName = stopScriptName;
	}

	@Override
	public String toString() {
		return gson.toJson(this);
	}

	public static AutomationProcess fromJson(String json) {
		return gson.fromJson(json, AutomationProcess.class);
	}
	
	public boolean getLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	//bpid, name, runid, started, finished, process_status, source_url, run_script, stop_script, node_name, lock
	public Object[] getData() {
		Object[] data = new Object[11];
		data[0] = bpId;
		data[1] = name;
		data[2] = runId;
		data[3] = started;
		data[4] = finished;
		data[5] = processStatus.toString();
		data[6] = sourceUrl;
		data[7] = runScriptName;
		data[8] = stopScriptName;
		data[9] = node == null ? null : node.getName();
		data[10] = lock;
		return data;
	}


}
