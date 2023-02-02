/**
 * 
 */
package ma.ab.banking.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ab.banking.entity.dto.AcctDpDto;
import ma.ab.banking.repo.AccountRepositoryImpl;
import ma.ab.banking.repo.AppCoreCallerImpl;
import ma.ab.banking.repo.AppCoreCallerRepository;
import ma.ab.banking.tran.excep.TransactionValidationException;

/**
 * @author a.bouabidi
 *
 */
@Service
public class TransactionEthixService {

	@Autowired
	private AppCoreCallerRepository appCoreCallerRepository ;
	
	@Autowired
	private AppCoreCallerImpl appCoreCallerImpl ;
	
	@Autowired
	private AccountRepositoryImpl accountRepositoryImpl;
	/*
	 * @return
	 */
	public int ExecuteTransactionSet (String acctNo, Double mt) {
		Date journee =accountRepositoryImpl.findByNativeQueryGetCurrentJounee();
		
		try {
			String r = accountRepositoryImpl.getTransTracerNo(0, journee);
			int n = legExecDpDebit (Integer.valueOf(108), journee, journee,r,905,acctNo, mt, 0, "Virement Cpt to Cpt",Integer.valueOf(1),Integer.valueOf(150),null,Integer.valueOf(600));
		} catch (TransactionValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	

	
	private int legExecDpCredit () {
		
		
		return 0;
	}
	
	
	private int legExecDpDebit(Integer pnservicesid, Date pdttrndt, Date pdteffectivedt, String pstracerno, 
			Integer pnxapitc, String psacctno, Double pnamt, Integer pncheckno1, // -- if pnphoenixtc <> 171 and null if not
			String pstrandescr, // up to 100 char
			Integer pnpostingbranchno, Integer pnphoenixtc, Integer pnphoenixcc,
			Integer pnchannel_id) throws TransactionValidationException {
		
		if (pnservicesid ==null || pdttrndt == null || pdteffectivedt == null
				|| pstracerno == null|| pnxapitc == null|| psacctno == null
				|| pnamt == null|| pstrandescr == null
				|| pnpostingbranchno == null|| pnphoenixtc == null
				||  pnchannel_id==null) {
			throw new TransactionValidationException("Field must not be null !!");					
		}
		//
		AcctDpDto act = accountRepositoryImpl.customNativeQuery(psacctno);
		//
		// --------------------------------------
		// --CALCULATED VALUE
		// --------------------------------------		
		String psappltype1 = act.getApplType ();
		String psaccttype1 = act.getAcctType ();
		String psdepln = act.getDpLn();// -- 'DP', 'GL','LN'
		Integer pnemployeeid = 0;// -- 0 default user
		String psisocurrency = act.getIsoCode ();// -- Account currency
		//
		// --------------------------------------
		// --DEFALUT VALUE
		// --------------------------------------
		String psproprietaryatm = "N";
		String psterminalid = "1";
		String psatmswitchid = "";
		String psmanneddevice = "N";
		String psorigreferenceno = null;
		String psoffline = "N";
		String psreversal = "N";
		String pspin = null;
		String psnewpin = null;
		String pscardno = null;
		Integer pnptid = 0;
		String psappltype2 = null;
		String psaccttype2 = null;
		String psacctno2 = null;
		Double pnamt2 = Double.valueOf(0);
		Integer pncheckno2 = null;
		Date pdtdate1 = null;
		Date pdtdate2 = null;
		Integer pnpayeeno = null;
		String psdevicelocation = null;
		String pspbupdated = "N";
		Integer pnsupervisorid = 0;
		String psversion = "3.0";
		Integer pntrantype = 0;
		Integer pssdacctid = 0;
		String psusername = "PHOENIX";
		Integer pndrawerno = 0;
		Double pnfloatamt1 = 0d;
		Double pnfloatamt2 = 0d;
		String psisocurrency2 = null;
		Double pnrate1 = 0d;
		Double pnrate2 = 0d;
		Double pnrate3 = 0d;
		Double pnrate4 = 0d;
		Double pnrate5 = 0d;
		Double pnrate6 = 0d;
		Integer pnrateindexid = 0;
		Double pnphoenixccamt=0d;
		String pstablename = null;
		String pstablekey = null;
		String pstablekeytype = null;
		String psstring1 = null;
		String psstring2 = null;
		String psstring3 = null;
		String psstring4 = null;
		Integer pninteger1 = 0;
		Integer pninteger2 = 0;
		Integer pndebug = 0;
		Integer nrc = 0;
		
		
		nrc = appCoreCallerImpl.execEthixTran2(pnservicesid, pdttrndt, pdteffectivedt, psterminalid, psproprietaryatm,
				psatmswitchid, psmanneddevice, pstracerno, psorigreferenceno, psoffline, psreversal, pspin, psnewpin,
				pscardno, pnxapitc, psisocurrency, pnptid, psappltype1, psaccttype1, psacctno, psappltype2, psaccttype2,
				psacctno2, pnamt, pnamt2, pncheckno1, pncheckno2, pdtdate1, pdtdate2, pnpayeeno, psdevicelocation,
				pstrandescr, pspbupdated, pnemployeeid, pnsupervisorid, psversion, pntrantype, psdepln, pssdacctid,
				psusername, pnpostingbranchno, pndrawerno, pnfloatamt1, pnfloatamt2, psisocurrency2, pnrate1, pnrate2,
				pnrate3, pnrate4, pnrate5, pnrate6, pnrateindexid, pnphoenixtc, pnphoenixcc, pnphoenixccamt,
				pstablename, pstablekey, pstablekeytype, psstring1, psstring2, psstring3, psstring4, pninteger1,
				pninteger2, pndebug);

		return nrc;
	}
	
	
	private int legExecGlCredit () {
		
		
		return 0;
	}
	
	private int legExecGlDebit () {
		
		
		return 0;
	}
	
	private int legExecLnCredit () {
		
		
		return 0;
	}
	
	private int legExecLnDebit () {
		
		
		return 0;
	}

}
