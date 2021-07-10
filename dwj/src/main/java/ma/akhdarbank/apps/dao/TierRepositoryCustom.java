package ma.akhdarbank.apps.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import ma.akhdarbank.apps.model.LabFMatching;
import ma.akhdarbank.apps.model.TierBatch;

@Repository
public class TierRepositoryCustom {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Transactional	
	public void updateTiersBatch (List<TierBatch.TierBatchReq> tiers, Long ticket) {		
		StopWatch timer = new StopWatch();
	    String sql = "insert into LAB_F_MATCHING (ETX_RIM, NOM, PRENOM, ID_BATCH) values(:idclient, :nom, :prenom, :idbatch)";
	    timer.start(); 
	    jdbcTemplate.batchUpdate(sql, new TierBatchPreparedStatementSetter(processRequestRetun(tiers,ticket)));
	    timer.stop();
	   // log.info("batchInsert -> Total time in seconds: " + timer.getTotalTimeSeconds());
		
	}
	
	
	
	private List<LabFMatching> processRequestRetun(List<TierBatch.TierBatchReq> tiers, Long ticket) {
		List<LabFMatching> list = new ArrayList<LabFMatching>();
		for (TierBatch.TierBatchReq t : tiers) {
			if (list==null)list = new ArrayList<LabFMatching>();
			LabFMatching l = new LabFMatching();
			l.setIdbatch(String.valueOf(ticket));
			l.setNom(t.getNom());
			l.setPrenom(t.getPrenom());
			l.setNumRim(t.getIdclient());			
		}
		
		return list;
	}

}
