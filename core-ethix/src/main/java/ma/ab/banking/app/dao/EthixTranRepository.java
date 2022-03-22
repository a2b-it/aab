/**
 * 
 */
package ma.ab.banking.app.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.ab.banking.app.config.MyBatisNativeCallableExecutor;
import ma.ab.banking.app.config.SqlParam;
import ma.ab.banking.tran.excep.TransactionException;

/**
 * @author a.bouabidi
 *
 */
@Component
public class EthixTranRepository {
	
	@Autowired
	MyBatisNativeCallableExecutor exec;
	
	public Integer callprocTest (Integer num) throws TransactionException {
		List<SqlParam>  params = new ArrayList();
				params.add(SqlParam.create("pn_param", num, Integer.class.getName()));
		return (Integer)exec.executeFunction(SqlParam.createOut("p",Integer.class.getName()),"", "psp_test", params);
		
		
	};
	
	public int  execEthixTran(Integer pnservicesid, Date pdttrandt, Date pdteffectivedt, String psterminalid,		
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
			String psstring3, String psstring4, Integer pninteger1, Integer pninteger2, Integer pndebug) throws TransactionException {
		List<SqlParam>  params = new ArrayList();
		params.add(SqlParam.create("pnservicesid",pnservicesid,Integer.class.getName()));
		params.add(SqlParam.create("pdttrandt",pdttrandt,Date.class.getName()));
		params.add(SqlParam.create("pdteffectivedt",pdteffectivedt,Date.class.getName()));
		params.add(SqlParam.create("psterminalid",psterminalid,String.class.getName()));
		params.add(SqlParam.create("psproprietaryatm",psproprietaryatm,String.class.getName()));
		params.add(SqlParam.create("psatmswitchid",psatmswitchid,String.class.getName()));
		params.add(SqlParam.create("psmanneddevice",psmanneddevice,String.class.getName()));
		params.add(SqlParam.create("psreferenceno",psreferenceno,String.class.getName()));
		params.add(SqlParam.create("psorigreferenceno",psorigreferenceno,String.class.getName()));
		params.add(SqlParam.create("psoffline",psoffline,String.class.getName()));
		params.add(SqlParam.create("psreversal",psreversal,String.class.getName()));
		params.add(SqlParam.create("pspin",pspin,String.class.getName()));
		params.add(SqlParam.create("psnewpin",psnewpin,String.class.getName()));
		params.add(SqlParam.create("pscardno",pscardno,String.class.getName()));
		params.add(SqlParam.create("pntrancode",pntrancode,Integer.class.getName()));
		params.add(SqlParam.create("psisocurrency",psisocurrency,String.class.getName()));
		params.add(SqlParam.create("pnptid",pnptid,Integer.class.getName()));
		params.add(SqlParam.create("psappltype1",psappltype1,String.class.getName()));
		params.add(SqlParam.create("psaccttype1",psaccttype1,String.class.getName()));
		params.add(SqlParam.create("psacctno1",psacctno1,String.class.getName()));
		params.add(SqlParam.create("psappltype2",psappltype2,String.class.getName()));
		params.add(SqlParam.create("psaccttype2",psaccttype2,String.class.getName()));
		params.add(SqlParam.create("psacctno2",psacctno2,String.class.getName()));
		params.add(SqlParam.create("pnamt1",pnamt1,Double.class.getName()));
		params.add(SqlParam.create("pnamt2",pnamt2,Double.class.getName()));
		params.add(SqlParam.create("pncheckno1",pncheckno1,Integer.class.getName()));
		params.add(SqlParam.create("pncheckno2",pncheckno2,Integer.class.getName()));
		params.add(SqlParam.create("pdtdate1",pdtdate1,Date.class.getName()));
		params.add(SqlParam.create("pdtdate2",pdtdate2,Date.class.getName()));
		params.add(SqlParam.create("pnpayeeno",pnpayeeno,Integer.class.getName()));
		params.add(SqlParam.create("psdevicelocation",psdevicelocation,String.class.getName()));
		params.add(SqlParam.create("psdescription",psdescription,String.class.getName()));
		params.add(SqlParam.create("pspbupdated",pspbupdated,String.class.getName()));
		params.add(SqlParam.create("pnemployeeid",pnemployeeid,Integer.class.getName()));
		params.add(SqlParam.create("pnsupervisorid",pnsupervisorid,Integer.class.getName()));
		params.add(SqlParam.create("psversion",psversion,String.class.getName()));
		params.add(SqlParam.create("pntrantype",pntrantype,Integer.class.getName()));
		params.add(SqlParam.create("psdeploan",psdeploan,String.class.getName()));
		params.add(SqlParam.create("pssdacctid",pssdacctid,Integer.class.getName()));
		params.add(SqlParam.create("psusername",psusername,String.class.getName()));
		params.add(SqlParam.create("pnbranchno",pnbranchno,Integer.class.getName()));
		params.add(SqlParam.create("pndrawerno",pndrawerno,Integer.class.getName()));
		params.add(SqlParam.create("pnfloatamt1",pnfloatamt1,Double.class.getName()));
		params.add(SqlParam.create("pnfloatamt2",pnfloatamt2,Double.class.getName()));
		params.add(SqlParam.create("psisocurrency2",psisocurrency2,String.class.getName()));
		params.add(SqlParam.create("pnrate1",pnrate1,Double.class.getName()));
		params.add(SqlParam.create("pnrate2",pnrate2,Double.class.getName()));
		params.add(SqlParam.create("pnrate3",pnrate3,Double.class.getName()));
		params.add(SqlParam.create("pnrate4",pnrate4,Double.class.getName()));
		params.add(SqlParam.create("pnrate5",pnrate5,Double.class.getName()));
		params.add(SqlParam.create("pnrate6",pnrate6,Double.class.getName()));
		params.add(SqlParam.create("pnrateindexid",pnrateindexid,Integer.class.getName()));
		params.add(SqlParam.create("pnphoenixtc",pnphoenixtc,Integer.class.getName()));
		params.add(SqlParam.create("pnphoenixcc",pnphoenixcc,Integer.class.getName()));
		params.add(SqlParam.create("pnphoenixccamt",pnphoenixccamt,Double.class.getName()));
		params.add(SqlParam.create("pstablename",pstablename,String.class.getName()));
		params.add(SqlParam.create("pstablekey",pstablekey,String.class.getName()));
		params.add(SqlParam.create("pstablekeytype",pstablekeytype,String.class.getName()));
		params.add(SqlParam.create("psstring1",psstring1,String.class.getName()));
		params.add(SqlParam.create("psstring2",psstring2,String.class.getName()));
		params.add(SqlParam.create("psstring3",psstring3,String.class.getName()));
		params.add(SqlParam.create("psstring4",psstring4,String.class.getName()));
		params.add(SqlParam.create("pninteger1",pninteger1,Integer.class.getName()));
		params.add(SqlParam.create("pninteger2",pninteger2,Integer.class.getName()));
		params.add(SqlParam.create("pndebug",pndebug,Integer.class.getName()));

		return (Integer)exec.executeFunction(SqlParam.createOut("",Integer.class.getName()),"atm", "psp_ex_main3", params);
	}

	public String  getTransTracerNo(int n, Date journee) throws TransactionException {
		List<SqlParam>  params = new ArrayList();
		params.add(SqlParam.create("pnEmpID",n,Integer.class.getName()));
		params.add(SqlParam.create("pdtEffectiveDt",journee,Date.class.getName()));
		params.add(SqlParam.create("pdtCreateDt",journee,Date.class.getName()));
		params.add(SqlParam.createInOut("rsTracerNo","",String.class.getName()));
		Integer i = (Integer)exec.executeFunction(SqlParam.createOut("",Integer.class.getName()),"sa", "psp_genrate_trans_tracer_no", params);
		return (i==0)?(String)params.get(3).getValue():null;
	}

}
