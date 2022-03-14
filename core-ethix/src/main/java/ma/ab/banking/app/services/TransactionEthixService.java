/**
 * 
 */
package ma.ab.banking.app.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ab.banking.app.dao.AccountMapper;
import ma.ab.banking.app.dao.EthixTranRepository;
import ma.ab.banking.app.entity.AcctDpDto;
import ma.ab.banking.app.entity.OutParam;
import ma.ab.banking.tran.excep.TransactionException;
import ma.ab.banking.tran.excep.TransactionValidationException;

/**
 * @author a.bouabidi
 *
 */
@Service
public class TransactionEthixService {

	@Autowired
	AccountMapper accountMapper;
	
	@Autowired
	EthixTranRepository tranRepo;
	
	public int ExecuteTransactionSet (String acctNoDebit,String acctNoCredit, Double mt) throws TransactionValidationException, TransactionException {
		Date journee =accountMapper.findByNativeQueryGetCurrentJounee();
		String referenceNum = tranRepo.getTransTracerNo(0, journee);
		AcctDpDto actDebit = accountMapper.getAccountInfos(acctNoDebit);
		AcctDpDto actCredit = accountMapper.getAccountInfos(acctNoCredit);
		
		int n = legExecDpDebit (actDebit,Integer.valueOf(108), journee, journee,referenceNum,acctNoDebit, mt, 0, "EMISSION VIRT EN FAVEUR DE"+actCredit.getTitulaire(),null,Integer.valueOf(600));	
		if(n!=0) {
			//run reverse of first leg
		}
		
		n = legExecDpCredit (actCredit,Integer.valueOf(108), journee, journee,referenceNum,acctNoCredit, mt, 0, "Réception VIRT DE"+actDebit.getTitulaire(),null,Integer.valueOf(600));
		if(n!=0) {
			//run reverse of 2 legs
		}
		return n;
	}
	
	
	
	private int legExecDpDebit(AcctDpDto act,Integer pnservicesid, Date pdttrndt, Date pdteffectivedt, String pstracerno, 
			String psacctno, Double pnamt, Integer pncheckno1, // -- if pnphoenixtc <> 171 and null if not
			String pstrandescr, // up to 100 char
			//Integer pnpostingbranchno,
			Integer pnphoenixcc,
			Integer pnchannel_id) throws TransactionValidationException, TransactionException {
		
		if (pnservicesid ==null || pdttrndt == null || pdteffectivedt == null
				|| pstracerno == null|| psacctno == null
				|| pnamt == null|| pstrandescr == null
				//|| pnpostingbranchno == null
				||  pnchannel_id==null) {
			throw new TransactionValidationException("Field must not be null !!");					
		}
		//		 
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
		int nrc = 0;
		
		
		nrc = tranRepo.execEthixTran(pnservicesid, pdttrndt, pdteffectivedt, psterminalid, psproprietaryatm,
				psatmswitchid, psmanneddevice, pstracerno, psorigreferenceno, psoffline, psreversal, pspin, psnewpin,
				pscardno, 905, psisocurrency, pnptid, psappltype1, psaccttype1, psacctno, psappltype2, psaccttype2,
				psacctno2, pnamt, pnamt2, pncheckno1, pncheckno2, pdtdate1, pdtdate2, pnpayeeno, psdevicelocation,
				pstrandescr, pspbupdated, pnemployeeid, pnsupervisorid, psversion, pntrantype, psdepln, pssdacctid,
				psusername, act.getBranchno(), pndrawerno, pnfloatamt1, pnfloatamt2, psisocurrency2, pnrate1, pnrate2,
				pnrate3, pnrate4, pnrate5, pnrate6, pnrateindexid, 150, pnphoenixcc, pnphoenixccamt,
				pstablename, pstablekey, pstablekeytype, psstring1, psstring2, psstring3, psstring4, pninteger1,
				pninteger2, pndebug);

		return nrc;
	}
	
	private int legExecDpCredit(AcctDpDto act,Integer pnservicesid, Date pdttrndt, Date pdteffectivedt, String pstracerno, 
			String psacctno, Double pnamt, Integer pncheckno1, // -- if pnphoenixtc <> 171 and null if not
			String pstrandescr, // up to 100 char
			//Integer pnpostingbranchno, 
			Integer pnphoenixcc,
			Integer pnchannel_id) throws TransactionValidationException, TransactionException {
		
		if (pnservicesid ==null || pdttrndt == null || pdteffectivedt == null
				|| pstracerno == null|| psacctno == null
				|| pnamt == null|| pstrandescr == null
				//|| pnpostingbranchno == null
				||  pnchannel_id==null) {
			throw new TransactionValidationException("Field must not be null !!");					
		}
		//
		
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
		int nrc = 0;
		
		
		nrc = tranRepo.execEthixTran(pnservicesid, pdttrndt, pdteffectivedt, psterminalid, psproprietaryatm,
				psatmswitchid, psmanneddevice, pstracerno, psorigreferenceno, psoffline, psreversal, pspin, psnewpin,
				pscardno, 904, psisocurrency, pnptid, psappltype1, psaccttype1, psacctno, psappltype2, psaccttype2,
				psacctno2, pnamt, pnamt2, pncheckno1, pncheckno2, pdtdate1, pdtdate2, pnpayeeno, psdevicelocation,
				pstrandescr, pspbupdated, pnemployeeid, pnsupervisorid, psversion, pntrantype, psdepln, pssdacctid,
				psusername, act.getBranchno(), pndrawerno, pnfloatamt1, pnfloatamt2, psisocurrency2, pnrate1, pnrate2,
				pnrate3, pnrate4, pnrate5, pnrate6, pnrateindexid, 101, pnphoenixcc, pnphoenixccamt,
				pstablename, pstablekey, pstablekeytype, psstring1, psstring2, psstring3, psstring4, pninteger1,
				pninteger2, pndebug);

		return nrc;
	}
	
	
	

}
