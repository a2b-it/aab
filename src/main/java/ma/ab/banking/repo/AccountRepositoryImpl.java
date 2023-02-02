/**
 * 
 */
package ma.ab.banking.repo;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ma.ab.banking.entity.dto.AcctDpDto;

/**
 * @author a.bouabidi
 *
 */
@Repository
@Transactional
public class AccountRepositoryImpl implements AccountRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;
	
	
	private static String sql_account_info ="SELECT a.acct_no acct_no, a.acct_type acct_type, a.appl_type appl_type, c.iso_code iso_code, 'DP' dpLn "
			+ "  FROM atm.dp_acct a, atm.dp_display d, atm.ad_gb_crncy c"
			+ "  WHERE a.acct_no = ?"
			+ "  AND a.acct_no = d.acct_no"
			+ "  AND a.acct_type = d.acct_type"
			+ "  AND d.crncy_id = c.crncy_id" ;
	
	private static String sql_journee_info ="SELECT last_to_dt+1 FROM atm.OV_CONTROL" ;
	 
	@Override
	public AcctDpDto customNativeQuery(String acctNum) {
		 Query query = entityManager.createNativeQuery(sql_account_info, AcctDpDto.class);
	        query.setParameter(1, acctNum );
		return (AcctDpDto) query.getSingleResult();
	}

	@Override
	public Date findByNativeQueryGetCurrentJounee() {
		 Query query = entityManager.createNativeQuery(sql_journee_info);
	       // query.setParameter(1, acctNum + "%");
		return (Date)query.getSingleResult();
	}
	
	public String getTransTracerNo (int n, Date journee) {		
		StoredProcedureQuery q = entityManager.createStoredProcedureQuery("PR_GENERATE_TRACER_NO");
		q.registerStoredProcedureParameter("NEMP", Integer.class, ParameterMode.IN);		
		q.registerStoredProcedureParameter("DJOURNEE", Date.class, ParameterMode.IN);
		q.registerStoredProcedureParameter("STRACENO", String.class, ParameterMode.OUT);
		q.setParameter("NEMP", 0);
		q.setParameter("DJOURNEE", journee);				
		
		return q.getOutputParameterValue("STRACENO").toString();
		
	}

}
