package remote.ct.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import remote.ct.dao.BusinessProcessRepository;
import remote.ct.dao.mappers.BusinessProcessRowMapper;
import remote.ct.model.AutomationProcess;
import remote.ct.model.SearchCriteria;


@Repository("JdbcBusinessProcessRepository")
public class JdbcBusinessProcessRepository implements BusinessProcessRepository {

	private static final Logger logger = LoggerFactory.getLogger(JdbcBusinessProcessRepository.class);

	public static final String GET_ALL_TMPL = "SELECT * FROM %s;";
	public static final String FIND_BY_ID_TMPL = "SELECT * FROM %s WHERE bpid=?;";
	public static final String UPSERT_TMPL = "INSERT INTO %s (bpid, name, runid, started, finished, process_status, source_url, run_script, stop_script, node_name, lock) "
			+ "values(?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT (bpid) DO UPDATE SET "
			+ "bpid=?, name=?, runid=?, started=?, finished=?, process_status=?, source_url=?, run_script=?, stop_script=?, node_name=?, lock=?;";
	public static final String FIND_ONE_AND_LOCK_TMPL = "SELECT * FROM %s WHERE %s FOR UPDATE LIMIT 1;";
	public static final String UPDATE_TMPL = "UPDATE %s SET lock=? WHERE bpid=?;";


	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<AutomationProcess> getAll(String table) {
		String q = String.format(GET_ALL_TMPL, table);
		List<AutomationProcess> res = jdbcTemplate.query(q, new BusinessProcessRowMapper());
		return res;
	}

	/**
	 *  Note: if record does not exist this code return an object with empty field bpid
	 */
	@Override
	public AutomationProcess findById(String table, String bpid) {
		String q = String.format(FIND_BY_ID_TMPL, table);
		AutomationProcess bp = jdbcTemplate.queryForObject(q, new Object[] { bpid }, new BusinessProcessRowMapper());
		return bp;
	}
	
	@Override
	public int unlock(String table, String bpid) {
		int res = jdbcTemplate.update(String.format(UPDATE_TMPL, table), false, bpid);
		return res;
	}

	
	@Override
	public AutomationProcess getAndLock(String table, SearchCriteria criteria) {
		String searchCriteria = criteria.getCriteria() == null ? "lock=false"
				: String.format("lock=false AND %s", criteria.getCriteria());
		String q = String.format(FIND_ONE_AND_LOCK_TMPL, table, searchCriteria);
		logger.info(q);
		List<AutomationProcess> res = jdbcTemplate.query(q, new BusinessProcessRowMapper());
		logger.info("size:"+res.size());
		if (!res.isEmpty()) {
			AutomationProcess available = res.get(0);
			logger.info("BP[0]: " + available);

			int updated = jdbcTemplate.update(String.format(UPDATE_TMPL, table), true, available.getBpId());
			if (updated > 0) {
				available.setLock(true);
				return available;
			}
		}
		return null;
	}


	@Override
	public int upsert(String table, AutomationProcess bp) {
		String query = String.format(UPSERT_TMPL, table);
		logger.debug("upsert: {}", bp);
		Object[] src = bp.getData();
		int len = src.length;
		Object[] params = new Object[2 * len];
		int idx = 0;
		// data for insert
		for (int i = 0; i < len; i++) {
			params[idx++] = src[i];
		}
		// data for update
		for (int i = 0; i < len; i++) {
			params[idx++] = src[i];
		}
		return jdbcTemplate.update(query, params);
	}

}
