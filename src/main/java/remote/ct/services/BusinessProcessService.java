package remote.ct.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import remote.ct.dao.BusinessProcessRepository;
import remote.ct.model.AutomationProcess;
import remote.ct.model.SearchCriteria;

@Service
public class BusinessProcessService {

	private final String repositoryName = "processes";
	
	@Autowired
	@Qualifier("JdbcBusinessProcessRepository")
	private BusinessProcessRepository repository;

	public BusinessProcessService() {
	}

	synchronized public void add(AutomationProcess businessProcess) {
		repository.upsert(repositoryName, businessProcess);
	}
	
	synchronized public AutomationProcess peek() {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setCriteria("process_status='CREATED'");
		AutomationProcess found = repository.getAndLock(repositoryName, criteria);
		return found;
	}
	
	synchronized public int unlock(AutomationProcess businessProcess) {
		return repository.unlock(repositoryName, businessProcess.getBpId());
	}
	
	synchronized public int upsert(AutomationProcess businessProcess) {
		return repository.upsert(repositoryName, businessProcess);
	}
	
}
