package remote.ct.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import remote.ct.ssh.Key;

public class KeyService {
	private static volatile KeyService instance = null;
	
	private Map<String,Key> creds = new HashMap<>();

	private KeyService() {
	}

	public static KeyService getInstance() {
		if (instance == null) {
			instance = new KeyService();
		}
		return instance;
	}
	
	public Optional<Key> get(String alias){
		return Optional.of(creds.getOrDefault(alias, null));
	}
	
	public void put(String alias, Key value) {
		creds.put(alias, value);
	}
	
}
