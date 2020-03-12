package remote.ct.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryRepository <T> {

	private final ConcurrentMap<String, T> repo = new ConcurrentHashMap<>();
	
	public List<T> get() {
		return new ArrayList<>(repo.values());
	}
	
	public T get(String key) {
		return repo.getOrDefault(key, null);
	}
	
	public T put(String key, T data) {
		return repo.put(key, data);
	}
}
