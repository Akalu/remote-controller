package remote.ct.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import remote.ct.model.AutomationProcess;
import remote.ct.model.Node;
import remote.ct.model.SearchCriteria;

@Component
public interface BusinessProcessRepository {

	List<AutomationProcess> getAll(String table);
	
	AutomationProcess findById(String table, String bpid);
	
	int upsert(String table, AutomationProcess data);

	AutomationProcess getAndLock(String table, SearchCriteria criteria);

	int unlock(String table, String bpid);

}
