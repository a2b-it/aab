package ma.akhdarbank.apps.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;
import ma.akhdarbank.apps.excp.DAOCallException;
import ma.akhdarbank.apps.model.LabFMatching;
import ma.akhdarbank.apps.model.TierBatch;
import ma.akhdarbank.apps.model.TierBatch.TierBatchRep;

@Repository
@Slf4j
public class TierRepositoryCustom {
	
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private BatchRepository batchRepo;
	
	
	@Transactional	
	public void updateTiersBatchReq (List<TierBatch.TierBatchReq> tiers, Long ticket) throws DAOCallException {		
		StopWatch timer = new StopWatch();	    
		String upsert_sql = 
				"    INSERT INTO LAB_F_MATCHING  (ETX_RIM, NOM, PRENOM, ID_BATCH, IDPERSONNE)\r\n"
				+ "        VALUES(:numRim, :nom, :prenom, :idbatch, :idpersonne)"
				;
		//String sql = "insert into LAB_F_MATCHING (ETX_RIM, NOM, PRENOM, ID_BATCH, IDPERSONNE) values(:idclient, :nom, :prenom, :idbatch, :idpersonne)";
	    timer.start(); 
	    
	    int[] counts = namedParameterJdbcTemplate.batchUpdate(upsert_sql, processRequestReturn(tiers,ticket));
	    int totalrows = Arrays.stream(counts).sum();
	    //
	    saveBatchStatusAT(totalrows, ticket);
	    /* not correct
	     * if (totalrows != tiers.size()) {
	    	
	    	throw new DAOCallException("Number of updated rows is diffenrent from client number");
	    }*/
	    timer.stop();
	   log.info("updateTiersBatch request rows = "+totalrows+" -> Total time in seconds: " + timer.getTotalTimeSeconds());
		
	}
	
	
	/**
	 *
	 * @param tiers
	 * @param ticket
	 * @throws DAOCallException
	 */
	@Transactional	
	public void updateTiersBatchRep (List<? extends TierBatch.TierBatchRep> tiers, String numTicket) throws DAOCallException {		
		StopWatch timer = new StopWatch();	    
		String upsert_sql = 
				"   Call upsert_LAB_F_MATCHING (:etx_rim ,:firstname ,:maidenname ,"
				+ ":surname ,:maidenname ,:refpiece ,:year ,:country ,:address ,:score ,:idpersonne , :msgerreur,"
				+ ":datejournee,:id_batch, :raisonsocial)"
				;
		//String sql = "insert into LAB_F_MATCHING (ETX_RIM, NOM, PRENOM, ID_BATCH, IDPERSONNE) values(:idclient, :nom, :prenom, :idbatch, :idpersonne)";
		//
		if (tiers!=null && tiers.get(0).msgerreur!=null) {
			saveBatchStatusRJ (Long.valueOf(numTicket),tiers.get(0).msgerreur );
			return;
		}
		// No matching
		if (tiers!=null && tiers.size()==1 && "NOMATCHING".equals(tiers.get(0).getMsgerreur())) {
			saveBatchStatusVA (Long.valueOf(numTicket));
			return;
		}
	    timer.start(); 	  
	    SqlParameterSource[] namedParameters = new SqlParameterSource[tiers.size()];
	    int i =0;
	    for (TierBatchRep  t : tiers) {
	    	MapSqlParameterSource namedParameter = new MapSqlParameterSource();
	    	  namedParameter.addValue("etx_rim", t.idclient);
	 	    	//	.addValue("nom", t.)
	 	    	//	.addValue("prenom", upsert_sql)
	    	 namedParameter.addValue("firstname", t.firstname);
	    	  namedParameter.addValue("middlename", t.middlename);
	    	namedParameter.addValue("surname", t.surname);
	    	  namedParameter.addValue("maidenname", t.maidenname);
	    	  namedParameter.addValue("refpiece", t.refpiece);
	    	  namedParameter.addValue("year", t.year);
	    	 namedParameter.addValue("country", t.country);
	    	 namedParameter.addValue("address", t.address);
	    	 namedParameter.addValue("score", t.score);
	    	 namedParameter.addValue("idpersonne", t.idpersonne);	    	 
	    	  namedParameter.addValue("msgerreur", t.msgerreur);
	    	 namedParameter.addValue("raisonsocial", t.raisonsocial);
	    	 namedParameter.addValue("datejournee", new Date ());
	    	 namedParameter.addValue("id_batch", numTicket);
	    	 namedParameters[i++]=namedParameter;
	    }	   
	    int[] counts = namedParameterJdbcTemplate.batchUpdate(upsert_sql,namedParameters);	   
	    //
	    saveBatchStatusVA( Arrays.stream(counts).sum(), Long.valueOf(numTicket));
	    /* 
	     * not correct
	     * if (totalrows != tiers.size()) {
	    	
	    	throw new DAOCallException("Number of updated rows is diffenrent from client number");
	    }*/
	    timer.stop();
	   log.info("updateTiersBatch response= -> Total time in seconds: " + timer.getTotalTimeSeconds());
		
	}
	
	
	
	private SqlParameterSource[]  processRequestReturn(List<TierBatch.TierBatchReq> tiers, Long ticket) {
		List<LabFMatching> list = new ArrayList<LabFMatching>();
		SqlParameterSource[] batch = null;
		for (TierBatch.TierBatchReq t : tiers) {
			if (list==null)list = new ArrayList<LabFMatching>();
			LabFMatching l = new LabFMatching();
			l.setIdbatch(String.valueOf(ticket));
			l.setNumRim(t.getIdclient());
			l.setNom(t.getNom());
			l.setPrenom(t.getPrenom());
			l.setNumRim(t.getIdclient());			
			//l.setIdpersonne(t.get);i
			list.add(l);
		}
		batch= SqlParameterSourceUtils.createBatch(list.toArray());
		return batch;
	}
	
	private SqlParameterSource saveBatchStatusAT(int total, Long numTicket) {
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("datejournee", new Date())
				.addValue("nombreligne", total)
				.addValue("numTicket", numTicket)
				.addValue("statut", "AT")
				.addValue("msgerreur", "");
		
		String upsert_batch= "INSERT INTO LAB_F_MATCHING_BATCH ( DATEJOURNEE,NOMBRELIGNE,NUMTICKET,STATUT,MSGERREUR) "
	    		+ "        VALUES( :datejournee, :nombreligne, :numTicket, :statut, :msgerreur)";
	    namedParameterJdbcTemplate.update(upsert_batch, namedParameters);
		return namedParameters;
	}
	
	private SqlParameterSource saveBatchStatusVA(int total, Long numTicket) {
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("datejournee", new Date())
				.addValue("nombreligne", total)
				.addValue("numTicket", numTicket)
				.addValue("statut", "VA")
				.addValue("msgerreur", "");
		
		String upsert_batch= "INSERT INTO LAB_F_MATCHING_BATCH ( DATEJOURNEE,NOMBRELIGNE,NUMTICKET,STATUT,MSGERREUR) "
	    		+ "        VALUES( :datejournee, :nombreligne, :numTicket, :statut, :msgerreur)";
	    namedParameterJdbcTemplate.update(upsert_batch, namedParameters);
		return namedParameters;
	}
	
	private SqlParameterSource saveBatchStatusRJ(Long numTicket, String msg) {
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("datejournee", new Date())
				.addValue("nombreligne", 0)
				.addValue("numTicket", numTicket)
				.addValue("statut", "RJ")
				.addValue("msgerreur", msg);
		
		String upsert_batch= "INSERT INTO LAB_F_MATCHING_BATCH ( DATEJOURNEE,NOMBRELIGNE,NUMTICKET,STATUT,MSGERREUR) "
	    		+ "        VALUES( :datejournee, :nombreligne, :numTicket, :statut, :msgerreur)";
	    namedParameterJdbcTemplate.update(upsert_batch, namedParameters);
		return namedParameters;
	}
	
	private SqlParameterSource saveBatchStatusVA(Long numTicket) {
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("datejournee", new Date())
				.addValue("nombreligne", 0)
				.addValue("numTicket", numTicket)
				.addValue("statut", "VA")
				.addValue("msgerreur", "No matching");
		
		String upsert_batch= "INSERT INTO LAB_F_MATCHING_BATCH ( DATEJOURNEE,NOMBRELIGNE,NUMTICKET,STATUT,MSGERREUR) "
	    		+ "        VALUES( :datejournee, :nombreligne, :numTicket, :statut, :msgerreur)";
	    namedParameterJdbcTemplate.update(upsert_batch, namedParameters);
		return namedParameters;
	}

}
