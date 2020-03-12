package remote.ct.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import remote.ct.model.AutomationProcess;
import remote.ct.model.Node;
import remote.ct.model.ProcessStatus;

public class BusinessProcessRowMapper implements RowMapper<AutomationProcess> {

	@Override
	public AutomationProcess mapRow(ResultSet rs, int rowNum) throws SQLException {
		AutomationProcess bp = new AutomationProcess();
		bp.setBpId(rs.getString("bpid"));
		bp.setName(rs.getString("name"));
		bp.setRunId(rs.getString("runid"));
		bp.setStarted(rs.getTimestamp("started"));
		bp.setFinished(rs.getTimestamp("finished"));
		bp.setProcessStatus(ProcessStatus.valueOf(rs.getString("process_status")));
		bp.setSourceUrl(rs.getString("source_url"));
		bp.setRunScriptName(rs.getString("run_script"));
		bp.setStopScriptName(rs.getString("stop_script"));
		String nodeName = rs.getString("node_name");
		if (nodeName != null) {
			Node node = new Node();
			node.setName(nodeName);
			bp.setNode(node);
		}
		return bp;
	}

}
