/**
 * 
 */
package ma.ab.banking.app.dao;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import ma.ab.banking.app.entity.AcctDpDto;
import ma.ab.banking.app.entity.OutParam;

/**
 * @author a.bouabidi
 *
 */
@Mapper
public interface AccountMapper {
	
	
	public AcctDpDto getAccountInfos(String acctNo);
	
	public Map<String, Object> execEthixTran(Integer pnservicesid, Date pdttrandt, Date pdteffectivedt, String psterminalid,		
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
	
	@Select("SELECT last_to_dt+1 FROM atm.OV_CONTROL")
	Date findByNativeQueryGetCurrentJounee ();
	
	
	
}
