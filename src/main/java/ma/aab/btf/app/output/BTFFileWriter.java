/**
 * 
 */
package ma.aab.btf.app.output;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * @author a.bouabidi
 *
 */
@Component
public class BTFFileWriter<T> {
	private File btf ;
	
	private Iterable<T> data;
	private String channelID;
	private int lineID;
	private String fileID;
	private Date dateJour;
	private String fileDescription;
	private String originBranch;
	private int noHoldRecords;
	private int noTotRecords;
	
	
	private BTFWriteBody btfWriteBody;
	private BTFWriteFooter btfWriteFooter;	
	private BTFWriteHeader btfWriteHeader;
	
	private String header;
	private String footer;
	
	public String writeHeader () {
		
		
		header = btfWriteHeader.write(dateJour, fileDescription,originBranch, dateJour, false);
		
		return header;
	};
	
	public String  writeBody (String fileID, String fromAcctType,String fromAcctNum,String fromAcctCur,String fromTran,
			String toAcctType,String toAcctNum,String toAcctCur,String toTran,String description,Double amount,Double buyRate,Double sellRate,boolean overDrawfromAcct,boolean allowPartialwithdrawal) {
		 
		
			String line = btfWriteBody.writeNeither(channelID,fileID,lineID,fromAcctType, fromAcctNum, fromAcctCur,fromTran,
										toAcctType, toAcctNum, toAcctCur,toTran,description,amount,dateJour,buyRate,sellRate,overDrawfromAcct,allowPartialwithdrawal);
		
		
			return line;
	};
	
	public String  writeFooter () {				
		footer = btfWriteFooter.write(dateJour, this.noHoldRecords,this.noTotRecords);
		return footer;
	};
	
	
	
}
