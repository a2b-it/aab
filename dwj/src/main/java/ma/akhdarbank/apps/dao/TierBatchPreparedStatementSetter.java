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
/*
 * 
 * 
 * UPDATE LAB_F_MATCHING\r\n"
				+ "    SET ETX_RIM = :idclient,\r\n"
				+ "        NOM = :nom,\r\n"
				+ "        PRENOM = :prenom,\r\n"
				+ "        IDBATCH = :idbatch,\r\n"
				+ "        IDPERSONNE = :idpersonne,\r\n"
				+ "    WHERE ETX_RIM = :idclient, IDBATCH = :idbatch, IDPERSONNE = :idpersonne;\r\n"
				+ "\r\n"
				+ "IF ( sql%rowcount = 0 )\r\n"
				+ "    THEN\r\n"
				+ "    INSERT INTO LAB_F_MATCHING  (ETX_RIM, NOM, PRENOM, ID_BATCH, IDPERSONNE)\r\n"
				+ "        VALUES (:idclient, :nom, :prenom, :idbatch, :idpersonne);\r\n"
 */
	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		
			LabFMatching th = tiersMatching.get(i);
			ps.setString(1, th.getNumRim());			
			ps.setString(2, th.getNom());
			ps.setString(3, th.getPrenom());
			if(th.getIdbatch()!=null)
				ps.setLong(4, Long.valueOf(th.getIdbatch()));
			if(th.getIdpersonne()!=null)
				ps.setLong(5,  Long.valueOf(th.getIdpersonne()));
			//
			ps.setString(6, th.getNumRim());					
			if(th.getIdbatch()!=null)
				ps.setLong(7, Long.valueOf(th.getIdbatch()));
			if(th.getIdpersonne()!=null)
				ps.setLong(8,  Long.valueOf(th.getIdpersonne()));
			//
			ps.setString(9, th.getNumRim());			
			ps.setString(10, th.getNom());
			ps.setString(11, th.getPrenom());
			if(th.getIdbatch()!=null)
				ps.setLong(12, Long.valueOf(th.getIdbatch()));
			if(th.getIdpersonne()!=null)
				ps.setLong(13,  Long.valueOf(th.getIdpersonne()));
			
		
	}

	@Override
	public int getBatchSize() {		
		return (tiersMatching != null) ? tiersMatching.size() : 0;
	}
	
	

}
