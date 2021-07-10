package ma.akhdarbank.apps.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import ma.akhdarbank.apps.model.LabFMatching;

public class TierBatchPreparedStatementSetter implements BatchPreparedStatementSetter {

	private List<LabFMatching> tiersMatching;
		

	public TierBatchPreparedStatementSetter(List<LabFMatching> tiersMatching) {
		super();
		this.tiersMatching = tiersMatching;
	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		try {
			LabFMatching th = tiersMatching.get(i);
			ps.setString(1, th.getNumRim());
			ps.setString(2, th.getNom());
			ps.setString(3, th.getPrenom());
			ps.setString(4, th.getIdbatch());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getBatchSize() {		
		return (tiersMatching != null) ? tiersMatching.size() : 0;
	}

}
