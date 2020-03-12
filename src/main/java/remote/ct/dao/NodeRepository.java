package remote.ct.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import remote.ct.model.Node;
import remote.ct.model.SearchCriteria;

@Component
public interface NodeRepository {
	
	List<Node> getAll(String table);
	
	int unlock(String table, String name);
	
	Node getAndLock(String table, SearchCriteria criteria);
	
	int upsert(String table, Node data);
}
