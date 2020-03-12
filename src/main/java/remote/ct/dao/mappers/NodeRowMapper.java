package remote.ct.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import remote.ct.model.Node;
import remote.ct.model.Status;

public class NodeRowMapper implements RowMapper<Node> {

	@Override
	public Node mapRow(ResultSet rs, int rowNum) throws SQLException {
		Node node = new Node();
		node.setName(rs.getString("name"));
		node.setHost(rs.getString("host"));
		node.setPort(rs.getInt("port"));
		node.setStatus(Status.valueOf(rs.getString("status")));
		node.setType(rs.getString("node_type"));
		node.setLock(rs.getBoolean("lock"));
		node.setCreds(rs.getString("creds"));
		return node;
	}

}
