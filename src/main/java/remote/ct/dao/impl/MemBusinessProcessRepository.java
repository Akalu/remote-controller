package remote.ct.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import remote.ct.dao.BusinessProcessRepository;
import remote.ct.model.AutomationProcess;
import remote.ct.model.ProcessStatus;
import remote.ct.model.SearchCriteria;

@Repository("InMemoryBusinessProcessRepository")
public class MemBusinessProcessRepository implements BusinessProcessRepository {
	
	private Map<String,InMemoryRepository<AutomationProcess>> repo = new HashMap<>();
	
	private InMemoryRepository<AutomationProcess> get(String table) {
		if (!repo.containsKey(table)) {
			repo.put(table, new InMemoryRepository<AutomationProcess>());
		}
		return repo.get(table);
	}


	@Override
	public List<AutomationProcess> getAll(String table) {
		return get(table).get();
	}

	@Override
	public AutomationProcess findById(String table, String bpid) {
		List<AutomationProcess> all = get(table).get();
		for (AutomationProcess bp : all) {
			if (bp.getBpId().equals(bpid)) {
				return bp;
			}
		}
		return new AutomationProcess();
	}

	@Override
	public int upsert(String table, AutomationProcess data) {
		AutomationProcess rec = get(table).put(data.getBpId(),data);
		return 1;
	}


	@Override
	public AutomationProcess getAndLock(String table, SearchCriteria criteria) {
		List<AutomationProcess> all = get(table).get();
		for (AutomationProcess bp : all) {
			if (!bp.getLock() && ProcessStatus.CREATED.equals(bp.getProcessStatus())) {
				bp.setLock(true);
				return bp;
			}
		}
		return null;
	}


	@Override
	public int unlock(String table, String bpid) {
		AutomationProcess rec = get(table).get(bpid);
		if (rec != null) {
			rec.setLock(false);
		}
		return 0;
	}

}
