package remote.ct.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import remote.ct.dao.DataTORepository;
import remote.ct.model.AutomationProcess;
import remote.ct.model.DataTO;
import remote.ct.services.QueueService;

/**
 * This is a web service class which will be exposed for rest service.
 *  
 * @since Version 1.0
 */
@RestController
public class Controller {

	@Autowired
	@Qualifier("JdbcAuditRepository")
	private DataTORepository repository;
	
	@Autowired
	private QueueService queueService;
	
	@GetMapping(value = "/audit/{table}")
	public ResponseEntity<List<?>> getAll(@PathVariable String table) {

		List<DataTO> res = repository.getAll(table);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PostMapping(value = "/processes")
	public ResponseEntity<?> add(@RequestBody AutomationProcess businessProcess) throws InterruptedException, ExecutionException {
		queueService.add(businessProcess);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleError(Exception e) {
		String error = (e == null) ? "Unknown error" : ("Error: " + e.getMessage());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}