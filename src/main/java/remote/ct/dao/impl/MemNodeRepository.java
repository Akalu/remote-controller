package remote.ct.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import remote.ct.dao.NodeRepository;
import remote.ct.model.Node;
import remote.ct.model.SearchCriteria;
import remote.ct.model.Status;

@Repository("InMemoryNodeRepository")
public class MemNodeRepository implements NodeRepository {
	
	private Map<String,InMemoryRepository<Node>> repo = new HashMap<>();
	
	private InMemoryRepository<Node> get(String table) {
		if (!repo.containsKey(table)) {
			repo.put(table, new InMemoryRepository<Node>());
		}
		return repo.get(table);
	}

	@Override
	public List<Node> getAll(String table) {
		return get(table).get();
	}

	@Override
	public int unlock(String table, String name) {
		Node node = get(table).get(name);
		if (node != null) {
			node.setLock(false);
			return 1;
		}
		return 0;
	}

	@Override
	public Node getAndLock(String table, SearchCriteria criteria) {
		List<Node> nodes = get(table).get();
		for (Node node : nodes) {
			if (!node.getLock() && Status.IDLE.equals(node.getStatus())) {
				node.setLock(true);
				return node;
			}
		}
		return null;
	}

	@Override
	public int upsert(String table, Node data) {
		get(table).put(data.getName(),data);
		return 1;
	}

}
