/**
 * 
 */
package ma.ab.banking.repo;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.ab.banking.entity.Account;



/**
 * @author a.bouabidi
 *
 */

@Repository
public interface AppCoreCallerRepository extends JpaRepository<Account,Long>{
	
	//,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?
	@Query(nativeQuery = true,value = "select atm.psp_ex_main30 (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) from dual" )
	public int execEthixTran(Integer pnservicesid, Date pdttrandt, Date pdteffectivedt, String psterminalid,		
			String psproprietaryatm, String psatmswitchid, String psmanneddevice, String psreferenceno,
			String psorigreferenceno, String psoffline, 
			String psreversal, String pspin, String psnewpin,String pscardno,Integer pntrancode, String psisocurrency,
			Integer pnptid,String psappltype1,	String psaccttype1, String psacctno1,String psappltype2, String psaccttype2, String psacctno2,
			Double pnamt1, Double pnamt2, 
			
			Integer pncheckno1, Integer pncheckno2, Date pdtdate1, Date pdtdate2,
			Integer pnpayeeno, String psdevicelocation, String psdescription, String pspbupdated, Integer pnemployeeid,//			
			Integer pnsupervisorid, String psversion, Integer pntrantype, String psdeploan, Integer pssdacctid,
			String psusername, Integer pnbranchno, Integer pndrawerno, Double pnfloatamt1, Double pnfloatamt2,
			String psisocurrency2, Double pnrate1, Double pnrate2, Double pnrate3, Double pnrate4, Double pnrate5,
			Double pnrate6, Integer pnrateindexid, Integer pnphoenixtc, Integer pnphoenixcc, Double pnphoenixccamt,			
			String pstablename, String pstablekey, String pstablekeytype, String psstring1, String psstring2,
			String psstring3, String psstring4, Integer pninteger1, Integer pninteger2, Integer pndebug);
	
	//@Procedure (procedureName  = "atm.psp_genrate_trans_tracer_no", outputParameterName ="rsTracerNo" )
	
	//@Query(value = "{select PR_GENERATE_TRACER_NO (?, ?,?,?) from dual}", nativeQuery = true)
	//public Map<String, Object> getTransTracerNo (@Param("NEMP") Integer empId, @Param("DJOURNEE")Date effDt,@Param("STRACENO") String num);
	
}
