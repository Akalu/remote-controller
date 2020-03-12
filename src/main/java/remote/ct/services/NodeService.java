package remote.ct.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import remote.ct.dao.NodeRepository;
import remote.ct.model.Node;
import remote.ct.model.SearchCriteria;
import remote.ct.model.Status;

/**
 *	Implements Round-Robin algorithm for the pool of open nodes
 * 
 */
@Service
public class NodeService {
	
	private final String repositoryName = "nodes";
	
	@Autowired
	@Qualifier("JdbcNodeRepository")
	private NodeRepository repository;

	public NodeService() {
	}

	synchronized public Optional<Node> getAvailableNode() {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setCriteria("status='IDLE'");// looking for both true: lock=false & status=IDLE
		Node node = repository.getAndLock(repositoryName, criteria);
		if (node != null) {
			node.setStatus(Status.BUSY);
			repository.upsert(repositoryName, node);
			return Optional.of(node);
		}
		return Optional.empty();
	}
	
	synchronized public void returnNode(Node node) {
		node.setStatus(Status.IDLE);
		repository.upsert(repositoryName, node);
		repository.unlock(repositoryName, node.getName());
	}

	
	public void addNode(Node node) {
		if (node.getName().contains(" ")) {
			throw new IllegalArgumentException("Nodes name must not contain white spaces");
		}
		repository.upsert(repositoryName, node);
	}

	public void addAllNodes(List<Node> nodes) {
		for (Node node : nodes) {
			addNode(node);
		}
	}

}
