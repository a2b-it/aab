/**
 * 
 */
package ma.ab.banking.app.services;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class XapiCParams {
	
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
	Integer pninteger2 = 600;
	Integer pndebug = 0;
	

}
