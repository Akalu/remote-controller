package remote.ct.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import remote.ct.dao.DataTORepository;
import remote.ct.model.DataTO;
import remote.ct.model.SearchCriteria;


@Repository("JdbcAuditRepository")
public class JdbcDataTORepository implements DataTORepository {
	
	private static final Logger logger = LoggerFactory.getLogger(JdbcDataTORepository.class);


	public static final String GET_ALL_TMPL = "SELECT * FROM %s;";
	public static final String UPSERT_TMPL = "INSERT INTO %s (id, lock, owner, data) values(?,?,?,?) ON CONFLICT (id) DO UPDATE SET lock=?, owner=?, data=?;";
	public static final String UPDATE_TMPL = "UPDATE %s SET lock=?, owner=? WHERE id=?;";
	public static final String UPDATE_ALL_TMPL = "UPDATE %s SET lock=?, owner=? WHERE owner=?;";
	public static final String FIND_ONE_AND_LOCK_TMPL = "SELECT * FROM %s WHERE %s FOR UPDATE LIMIT 1;";
	public static final String UPDATE_LOCK_TMPL = "UPDATE %s SET lock=?, owner=? WHERE id=?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DataTO> getAll(String table) {
		return jdbcTemplate.query(String.format(GET_ALL_TMPL, table),
				(rs, rowNum) -> new DataTO(rs.getString("id"), rs.getString("owner"), rs.getBoolean("lock"), rs.getString("data")));
	}

	@Override
	@Transactional
	public int upsert(String table, String id, DataTO data) {
		logger.debug("upsert: {}" , data);
		int res = jdbcTemplate.update(String.format(UPSERT_TMPL, table), id, data.getLock(),
				data.getOwner(), data.getData(), data.getLock(), data.getOwner(), data.getData());
		return res;
	}

	@Override
	@Transactional
	public boolean unlock(String table, String id) {
		int res = jdbcTemplate.update(String.format(UPDATE_TMPL, table), false, null, id);
		return res == 1;
	}

	@Override
	@Transactional
	public long unlockAll(String table, SearchCriteria criteria) {
		int res = jdbcTemplate.update(String.format(UPDATE_ALL_TMPL, table), false, "", criteria.getSignature());
		return (long) res;
	}

	@Override
	@Transactional
	public DataTO getAndLock(String table, SearchCriteria criteria) {
		String searchCriteria = criteria.getCriteria() == null ? "lock=false" : String.format("lock=false AND %s",criteria.getCriteria());
		List<DataTO> res = jdbcTemplate.query(String.format(FIND_ONE_AND_LOCK_TMPL, table, searchCriteria),
				(rs, rowNum) -> new DataTO(rs.getString("id"), rs.getString("owner"), rs.getBoolean("lock"), rs.getString("data")));
		if (!res.isEmpty()) {
			DataTO found = res.get(0);
			logger.debug("data[0]: {}" , found);
			
			int updated = jdbcTemplate.update(String.format(UPDATE_LOCK_TMPL, table), true, criteria.getSignature(), found.getId());
			if (updated > 0) {
				found.setLock(true);
				found.setOwner(criteria.getSignature());
				return found;
			}
		}
		return null;
	}

}
