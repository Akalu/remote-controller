package remote.ct.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import remote.ct.model.DataTO;
import remote.ct.model.SearchCriteria;

@Component
public interface DataTORepository {
	
	List<DataTO> getAll(String table);

	boolean unlock(String table, String id);

	long unlockAll(String table, SearchCriteria signature);

	DataTO getAndLock(String table, SearchCriteria criteria);

	int upsert(String table, String id, DataTO data);

}
