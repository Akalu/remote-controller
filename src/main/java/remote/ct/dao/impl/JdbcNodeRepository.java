package remote.ct.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import remote.ct.dao.NodeRepository;
import remote.ct.dao.mappers.NodeRowMapper;
import remote.ct.model.Node;
import remote.ct.model.SearchCriteria;


@Repository("JdbcNodeRepository")
public class JdbcNodeRepository implements NodeRepository {

	private static final Logger logger = LoggerFactory.getLogger(JdbcNodeRepository.class);

	public static final String GET_ALL_TMPL = "SELECT * FROM %s;";
	public static final String FIND_BY_NAME_TMPL = "SELECT * FROM %s WHERE name=?;";
	public static final String UPDATE_TMPL = "UPDATE %s SET lock=? WHERE name=?;";
	public static final String UPSERT_TMPL = "INSERT INTO %s (name, host, port, status, node_type, creds, lock) "
			+ "values(?,?,?,?,?,?, ?) ON CONFLICT (name) DO UPDATE SET "
			+ "name=?, host=?, port=?, status=?, node_type=?, creds=?, lock=?;";
	public static final String FIND_ONE_AND_LOCK_TMPL = "SELECT * FROM %s WHERE %s FOR UPDATE LIMIT 1;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Node> getAll(String table) {
		String q = String.format(GET_ALL_TMPL, table);
		List<Node> res = jdbcTemplate.query(q, new NodeRowMapper());
		return res;
	}

	@Override
	public int unlock(String table, String name) {
		int res = jdbcTemplate.update(String.format(UPDATE_TMPL, table), false, name);
		return res;
	}

	@Override
	public Node getAndLock(String table, SearchCriteria criteria) {
		String searchCriteria = criteria.getCriteria() == null ? "lock=false"
				: String.format("lock=false AND %s", criteria.getCriteria());
		String q = String.format(FIND_ONE_AND_LOCK_TMPL, table, searchCriteria);
		logger.info(q);
		List<Node> res = jdbcTemplate.query(q, new NodeRowMapper());
		logger.info("size:"+res.size());
		if (!res.isEmpty()) {
			Node availableNode = res.get(0);
			logger.info("Node[0]: " + availableNode);

			int updated = jdbcTemplate.update(String.format(UPDATE_TMPL, table), true, availableNode.getName());
			if (updated > 0) {
				availableNode.setLock(true);
				return availableNode;
			}
		}
		return null;
	}

	@Override
	public int upsert(String table, Node data) {
		String query = String.format(UPSERT_TMPL, table);
		logger.info("upsert: {}", data);
		Object[] src = data.getData();
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
