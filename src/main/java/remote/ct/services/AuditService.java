package remote.ct.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import remote.ct.dao.DataTORepository;
import remote.ct.model.DataTO;

@Service
public class AuditService {
	
	private final String repositoryName = "process_audit";
	
	@Autowired
	@Qualifier("JdbcAuditRepository")
	private DataTORepository repository;

	public AuditService() {
	}

	public int save(String id, Object data) {
		DataTO d = new DataTO();
		d.setId(id);
		d.setData(data == null ? "N/A" : data.toString());
		return repository.upsert(repositoryName, id, d);
	}
}
