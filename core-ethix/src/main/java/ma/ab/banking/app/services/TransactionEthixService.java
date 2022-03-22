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
		
		int n = legExecDpDebit (actDebit,Integer.valueOf(108), journee, journee,referenceNum, mt, 0, "EMISSION VIRT EN FAVEUR DE "+actCredit.getTitulaire(),null,Integer.valueOf(600));	
		if(n!=0) {
			//run reverse of first leg
		}
		
		n = legExecDpCredit (actCredit,Integer.valueOf(108), journee, journee,referenceNum, mt, 0, "Réception VIRT DE "+actDebit.getTitulaire(),null,Integer.valueOf(600));
		if(n!=0) {
			//run reverse of 2 legs
		}
		return n;
	}
	
	
	
	private int legExecDpDebit(AcctDpDto act,Integer pnservicesid, Date pdttrndt, Date pdteffectivedt, String pstracerno, 
			Double pnamt, Integer pncheckno1, // -- if pnphoenixtc <> 171 and null if not
			String pstrandescr, // up to 100 char
			//Integer pnpostingbranchno,
			Integer pnphoenixcc,
			Integer pnchannel_id) throws TransactionValidationException, TransactionException {
		
		if (pnservicesid ==null || pdttrndt == null || pdteffectivedt == null
				|| pstracerno == null
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
		XapiCParams xapic = new XapiCParams();
		int nrc = 0;
		
		
		nrc = tranRepo.execEthixTran(pnservicesid, pdttrndt, pdteffectivedt, xapic.psterminalid, xapic.psproprietaryatm,
				xapic.psatmswitchid, xapic.psmanneddevice, pstracerno, xapic.psorigreferenceno, xapic.psoffline, xapic.psreversal, xapic.pspin, xapic.psnewpin,
				xapic.pscardno, 905, psisocurrency, xapic.pnptid, psappltype1, psaccttype1, act.getAcctNo(), xapic.psappltype2, xapic.psaccttype2,
				xapic.psacctno2, pnamt, xapic.pnamt2, pncheckno1, xapic.pncheckno2, xapic.pdtdate1, xapic.pdtdate2, xapic.pnpayeeno, xapic.psdevicelocation,
				pstrandescr, xapic.pspbupdated, pnemployeeid, xapic.pnsupervisorid, xapic.psversion, xapic.pntrantype, psdepln, xapic.pssdacctid,
				xapic.psusername, act.getBranchno(), xapic.pndrawerno, xapic.pnfloatamt1, xapic.pnfloatamt2, xapic.psisocurrency2, xapic.pnrate1, xapic.pnrate2,
				xapic.pnrate3, xapic.pnrate4, xapic.pnrate5, xapic.pnrate6, xapic.pnrateindexid, 150, pnphoenixcc, xapic.pnphoenixccamt,
				xapic.pstablename, xapic.pstablekey, xapic.pstablekeytype, xapic.psstring1, xapic.psstring2, xapic.psstring3, xapic.psstring4, xapic.pninteger1,
				xapic.pninteger2, xapic.pndebug);

		return nrc;
	}
	
	private int legExecDpCredit(AcctDpDto act,Integer pnservicesid, Date pdttrndt, Date pdteffectivedt, String pstracerno, 
			Double pnamt, Integer pncheckno1, // -- if pnphoenixtc <> 171 and null if not
			String pstrandescr, // up to 100 char
			//Integer pnpostingbranchno, 
			Integer pnphoenixcc,
			Integer pnchannel_id) throws TransactionValidationException, TransactionException {
		
		if (pnservicesid ==null || pdttrndt == null || pdteffectivedt == null
				|| pstracerno == null
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
		XapiCParams xapic = new XapiCParams();
		int nrc = 0;
		
		
		nrc = tranRepo.execEthixTran(pnservicesid, pdttrndt, pdteffectivedt, xapic.psterminalid, xapic.psproprietaryatm,
				xapic.psatmswitchid, xapic.psmanneddevice, pstracerno, xapic.psorigreferenceno, xapic.psoffline, xapic.psreversal, xapic.pspin, xapic.psnewpin,
				xapic.pscardno, 904, psisocurrency, xapic.pnptid, psappltype1, psaccttype1, act.getAcctNo(), xapic.psappltype2, xapic.psaccttype2,
				xapic.psacctno2, pnamt, xapic.pnamt2, pncheckno1, xapic.pncheckno2, xapic.pdtdate1, xapic.pdtdate2, xapic.pnpayeeno, xapic.psdevicelocation,
				pstrandescr, xapic.pspbupdated, pnemployeeid, xapic.pnsupervisorid, xapic.psversion, xapic.pntrantype, psdepln, xapic.pssdacctid,
				xapic.psusername, act.getBranchno(), xapic.pndrawerno, xapic.pnfloatamt1, xapic.pnfloatamt2, xapic.psisocurrency2, xapic.pnrate1, xapic.pnrate2,
				xapic.pnrate3, xapic.pnrate4, xapic.pnrate5, xapic.pnrate6, xapic.pnrateindexid, 101, pnphoenixcc, xapic.pnphoenixccamt,
				xapic.pstablename, xapic.pstablekey, xapic.pstablekeytype, xapic.psstring1, xapic.psstring2, xapic.psstring3, xapic.psstring4, xapic.pninteger1,
				xapic.pninteger2, xapic.pndebug);

		return nrc;
	}
	
	
	

}
