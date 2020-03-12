package remote.ct.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;


public class DataTO implements Serializable {

	private static final long serialVersionUID = 2357113637981299253L;
	
	public static final Gson gson = new Gson();
	public static final Type mapType = new TypeToken<Map<String, String>>(){}.getType();

	@SerializedName("id") 
	private String id;
	
	@SerializedName("lock") 
	private Boolean lock = false;

	@SerializedName("owner") 
	private String owner;

	@SerializedName("data") 
	private String data;
	
	public DataTO() {
	}
	
	public DataTO(String id, String owner, Boolean lock, String data) {
		this.id = id;
		this.lock = lock;
		this.owner = owner;
		this.data = data;
	}


	public String getId() {
		return id;
	}

	public void setId(String key) {
		this.id = key;
	}


	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void unlock() {
		this.lock = false;
		this.owner = null;
	}

	public synchronized Boolean lock(String owner) {
		if (Boolean.TRUE.equals(!this.lock)) {
			this.lock = true;
			this.owner = owner;
			return true;
		}
		return false;
	}

	public Boolean getLock() {
		return lock;
	}

	public void setLock(Boolean locked) {
		this.lock = locked;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return gson.toJson(this);
	}
	
	public static DataTO fromJson(String json) {
		return gson.fromJson(json, DataTO.class);
	}
	
}
