package remote.ct.model;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class Node {
	public static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	public static final Type mapType = new TypeToken<Map<String, String>>(){}.getType();

	@Expose
	private String name;
	@Expose
	private String host;
	@Expose
	private Integer port;
	@Expose
	private String type;
	@Expose
	private Status status;
	@Expose
	private String creds;// alias
	@Expose
	private Boolean lock = false;
	
	public Node() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Boolean getLock() {
		return lock;
	}
	public void setLock(Boolean lock) {
		this.lock = lock;
	}
	
	public String getCreds() {
		return creds;
	}
	public void setCreds(String creds) {
		this.creds = creds;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return gson.toJson(this);
	}
	
	public static Node fromJson(String json) {
		return gson.fromJson(json, Node.class);
	}

	// name, host, port, status, type, lock
	public Object[] getData() {
		Object[] data = new Object[7];
		data[0] = name;
		data[1] = host;
		data[2] = port;
		data[3] = status.toString();
		data[4] = type;
		data[5] = creds;
		data[6] = lock;
		return data;
	}

}
