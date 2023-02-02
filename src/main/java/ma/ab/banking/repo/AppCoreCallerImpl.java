/**
 * 
 */
package ma.ab.banking.repo;

import java.sql.Types;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 * @author a.bouabidi
 *
 */
@Repository
public class AppCoreCallerImpl  {
	
	private final String psp_ex_main30="psp_ex_main30";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	public int execEthixTran2(Integer pnservicesid, Date pdttrandt, Date pdteffectivedt, String psterminalid,
			String psproprietaryatm, String psatmswitchid, String psmanneddevice, String psreferenceno,
			String psorigreferenceno, String psoffline, String psreversal, String pspin, String psnewpin,
			String pscardno, Integer pntrancode, String psisocurrency, Integer pnptid, String psappltype1,
			String psaccttype1, String psacctno1, String psappltype2, String psaccttype2, String psacctno2,
			Double pnamt1, Double pnamt2, Integer pncheckno1, Integer pncheckno2, Date pdtdate1, Date pdtdate2,
			Integer pnpayeeno, String psdevicelocation, String psdescription, String pspbupdated, Integer pnemployeeid,
			Integer pnsupervisorid, String psversion, Integer pntrantype, String psdeploan, Integer pssdacctid,
			String psusername, Integer pnbranchno, Integer pndrawerno, Double pnfloatamt1, Double pnfloatamt2,
			String psisocurrency2, Double pnrate1, Double pnrate2, Double pnrate3, Double pnrate4, Double pnrate5,
			Double pnrate6, Integer pnrateindexid, Integer pnphoenixtc, Integer pnphoenixcc, Double pnphoenixccamt,
			String pstablename, String pstablekey, String pstablekeytype, String psstring1, String psstring2,
			String psstring3, String psstring4, Integer pninteger1, Integer pninteger2, Integer pndebug) {
		// TODO Auto-generated method stub
		
		SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withCatalogName("atm").withFunctionName(psp_ex_main30);
		call.addDeclaredParameter(new SqlParameter("pnservicesid",Types.INTEGER));
		
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("pnservicesid", pnservicesid,Types.INTEGER)
				.addValue("pdttrandt", pdttrandt,Types.DATE);
		
		//call.addDeclaredParameter(in);
		//MapSqlParameterSource paramMap= new MapSqlParameterSource();
		//params.
		//paramMap.addValue("pnservicesid",new SqlParameterValue(java.sql.Types.INTEGER,pnservicesid));
		//paramMap.addValue("pdttrandt", new SqlParameterValue(java.sql.Types.DATE,pdttrandt));
		/*if( pdteffectivedt !=null)paramMap.addValue("pdteffectivedt", pdteffectivedt);
		if( psterminalid !=null)paramMap.addValue("psterminalid", psterminalid);
		if( psproprietaryatm !=null)paramMap.addValue("psproprietaryatm", psproprietaryatm);
		if( psatmswitchid !=null)paramMap.addValue("psatmswitchid", psatmswitchid);
		if( psmanneddevice !=null)paramMap.addValue("psmanneddevice", psmanneddevice);
		if( psreferenceno !=null)paramMap.addValue("psreferenceno", psreferenceno);
		if( psorigreferenceno !=null)paramMap.addValue("psorigreferenceno", psorigreferenceno);
		if( psoffline !=null)paramMap.addValue("psoffline", psoffline);
		if( psreversal !=null)paramMap.addValue("psreversal", psreversal);
		if( pspin !=null)paramMap.addValue("pspin", pspin);
		if( psnewpin !=null)paramMap.addValue("psnewpin", psnewpin);
		if( pscardno !=null)paramMap.addValue("pscardno", pscardno);
		if( pntrancode !=null)paramMap.addValue("pntrancode", pntrancode);
		if( psisocurrency !=null)paramMap.addValue("psisocurrency", psisocurrency);
		if( pnptid !=null)paramMap.addValue("pnptid", pnptid);
		if( psappltype1 !=null)paramMap.addValue("psappltype1", psappltype1);
		if( psaccttype1 !=null)paramMap.addValue("psaccttype1", psaccttype1);
		if( psacctno1 !=null)paramMap.addValue("psacctno1", psacctno1);
		if( psappltype2 !=null)paramMap.addValue("psappltype2", psappltype2);
		if( psaccttype2 !=null)paramMap.addValue("psaccttype2", psaccttype2);
		if( psacctno2 !=null)paramMap.addValue("psacctno2", psacctno2);
		if( pnamt1 !=null)paramMap.addValue("pnamt1", pnamt1);
		if( pnamt2 !=null)paramMap.addValue("pnamt2", pnamt2);
		if( pncheckno1 !=null)paramMap.addValue("pncheckno1", pncheckno1);
		if( pncheckno2 !=null)paramMap.addValue("pncheckno2", pncheckno2);
		if( pdtdate1 !=null)paramMap.addValue("pdtdate1", pdtdate1);
		if( pdtdate2 !=null)paramMap.addValue("pdtdate2", pdtdate2);
		if( pnpayeeno !=null)paramMap.addValue("pnpayeeno", pnpayeeno);
		if( psdevicelocation !=null)paramMap.addValue("psdevicelocation", psdevicelocation);
		if( psdescription !=null)paramMap.addValue("psdescription", psdescription);
		if( pspbupdated !=null)paramMap.addValue("pspbupdated", pspbupdated);
		if( pnemployeeid !=null)paramMap.addValue("pnemployeeid", pnemployeeid);
		if( pnsupervisorid !=null)paramMap.addValue("pnsupervisorid", pnsupervisorid);
		if( psversion !=null)paramMap.addValue("psversion", psversion);
		if( pntrantype !=null)paramMap.addValue("pntrantype", pntrantype);
		if( psdeploan !=null)paramMap.addValue("psdeploan", psdeploan);
		if( pssdacctid !=null)paramMap.addValue("pssdacctid", pssdacctid);
		if( psusername !=null)paramMap.addValue("psusername", psusername);
		if( pnbranchno !=null)paramMap.addValue("pnbranchno", pnbranchno);
		if( pndrawerno !=null)paramMap.addValue("pndrawerno", pndrawerno);
		if( pnfloatamt1 !=null)paramMap.addValue("pnfloatamt1", pnfloatamt1);
		if( pnfloatamt2 !=null)paramMap.addValue("pnfloatamt2", pnfloatamt2);
		if( psisocurrency2 !=null)paramMap.addValue("psisocurrency2", psisocurrency2);
		if( pnrate1 !=null)paramMap.addValue("pnrate1", pnrate1);
		if( pnrate2 !=null)paramMap.addValue("pnrate2", pnrate2);
		if( pnrate3 !=null)paramMap.addValue("pnrate3", pnrate3);
		if( pnrate4 !=null)paramMap.addValue("pnrate4", pnrate4);
		if( pnrate5 !=null)paramMap.addValue("pnrate5", pnrate5);
		if( pnrate6 !=null)paramMap.addValue("pnrate6", pnrate6);
		if( pnrateindexid !=null)paramMap.addValue("pnrateindexid", pnrateindexid);
		if( pnphoenixtc !=null)paramMap.addValue("pnphoenixtc", pnphoenixtc);
		if( pnphoenixcc !=null)paramMap.addValue("pnphoenixcc", pnphoenixcc);
		if( pnphoenixccamt !=null)paramMap.addValue("pnphoenixccamt", pnphoenixccamt);
		if( pstablename !=null)paramMap.addValue("pstablename", pstablename);
		if( pstablekey !=null)paramMap.addValue("pstablekey", pstablekey);
		if( pstablekeytype !=null)paramMap.addValue("pstablekeytype", pstablekeytype);
		if( psstring1 !=null)paramMap.addValue("psstring1", psstring1);
		if( psstring2 !=null)paramMap.addValue("psstring2", psstring2);
		if( psstring3 !=null)paramMap.addValue("psstring3", psstring3);
		if( psstring4 !=null)paramMap.addValue("psstring4", psstring4);
		if( pninteger1 !=null)paramMap.addValue("pninteger1", pninteger1);
		if( pninteger2 !=null)paramMap.addValue("pninteger2", pninteger2);
		if( pndebug !=null)paramMap.addValue("pndebug", pndebug);*/
		
		
		return call.executeFunction(Integer.class,in );
	}

}
