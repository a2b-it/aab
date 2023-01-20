/**
 * 
 */
package ma.aab.btf.app.output;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author a.bouabidi
 *
 */
@Component
public class BTFWriteBody {
	enum HOLDRELEASE{
		N,
		H,
		R
	}
	final SimpleDateFormat sf = new SimpleDateFormat("MMddyyyy");
	final DecimalFormat df16 = new DecimalFormat("00000000000000.00");
	final DecimalFormat dfrate = new DecimalFormat("00000000000000.000000");
	
	static HOLDRELEASE HRN;
	

	

	
	public String writeNeither ( String channelID,String fileID, int lineID,String fromAcctType,
			String fromAcctNum, String fromAcctCur,String fromTranCode, String toAcctType,
			String toAcctNum, String toAcctCur, String toTranCode,    
			String Description, Double amount, Date dateTran,
			Double buyRate,Double sellRate, boolean OverDrawfromAcct,boolean AllowPartialwithdrawal ) {
		StringBuilder b = new StringBuilder();
		b.append("N")
			.append(StringUtils.rightPad(" ", 8,' '))	//Expiry Date					
			.append(StringUtils.rightPad(" ", 60,' ')) //String HoldInstruction	60 bytes
			.append(StringUtils.rightPad(" ", 20,' ')) //String HoldReference	20 bytes			
			.append(((OverDrawfromAcct)?'Y':'N')) //boolean OverDrawfromAcct	1 bytes
			.append(((AllowPartialwithdrawal)?'Y':'N')) //boolean AllowPartialwithdrawal  FROM acct	1 bytes
			.append(StringUtils.rightPad(" ", 20,' ')) //String CheckNumber	20 bytes
			.append(StringUtils.leftPad(channelID, 5,'0')) //String ChannelID	5 bytes
			.append(StringUtils.rightPad(fileID, 9,' ')) //String FileID	9 bytes
			.append(StringUtils.leftPad(""+lineID, 6,'0')) //String LineID	6
			.append(StringUtils.rightPad(" ", 45,' ')) //String Filler	45
			.append(StringUtils.rightPad(fromAcctType, 3,' ')) //String FROM Acct Type	3 bytes
			.append(StringUtils.rightPad(fromAcctNum, 60,' ')) //String FROM Acct Number	60 bytes
			.append(StringUtils.rightPad(fromAcctCur, 3,' ')) //String  FROM Currency Code	3 bytes
			.append(StringUtils.rightPad(fromTranCode, 3,' ')) //	String FROM Tran Code	3 bytes
			.append(StringUtils.rightPad(Description, 40,' ')) //String FROM Description	40 bytes
			
			.append(StringUtils.leftPad(df16.format(amount).replace(",","."), 26,'0')) //Decimal FROM Amount	26 bytes
			.append(StringUtils.rightPad(" ", 2,' ')) //String Filler	2 bytes
			.append(StringUtils.rightPad("", 8,' ')) //Date TO Acct Value Date	8 bytes
			
			.append(StringUtils.rightPad(toAcctType, 3,' ')) //String To Acct Type	3 bytes
			.append(StringUtils.rightPad(toAcctNum, 60,' ')) //String To Acct Number	60 bytes
			.append(StringUtils.rightPad(toAcctCur, 3,' ')) //String  To Currency Code	3 bytes
			.append(StringUtils.rightPad(toTranCode, 3,' ')) //	String To Tran Code	3 bytes			
			.append(StringUtils.rightPad(Description, 40,' ')) //String To Description	40 bytes
			.append(StringUtils.leftPad(df16.format(amount).replace(",","."), 26,'0')) //Decimal To Amount	26 bytes			
			.append(StringUtils.rightPad("", 1,' ')) //boolean Exclude Auto Retry	1 bytes			
			.append(StringUtils.rightPad("", 1,' ')) //boolean bank_cr_init	1 bytes
			.append(StringUtils.leftPad(dfrate.format(buyRate).replace(",","."), 26,'0')) //String Buy Rate	26 bytes
			.append(StringUtils.leftPad(dfrate.format(sellRate).replace(",","."), 26,'0'))//Sell Rate
			.append(StringUtils.rightPad("", 2,' ')) //String Filler	2 bytes
			.append(StringUtils.rightPad("", 8,' ')) //Date FROM Acct Value Date	8 bytes			
			.append(StringUtils.rightPad(sf.format(dateTran), 8,' ')) //Date Tran Effective Date	8 bytes
			
			;

		
		return b.toString();
	}
	


	public static void main(String[] args) {
		BTFWriteBody b = new BTFWriteBody ();
		String line = b.writeNeither( "200", "file1", 1, "101", "100000456", "MAD", "163", "119","100000457", "MAD","300",  "Transaction test", 1998.00, new Date(),1d,1d,false,false);
				System.out.println(line);
	}

}
