/**
 * 
 */
package ma.aab.btf.app.output;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author a.bouabidi
 *
 */
@Component
public class BTFWriteFooter {
	private final String  FileEndRecord="**"; 
	final SimpleDateFormat formater = new SimpleDateFormat("MMddyyyy");
	
	
	
	public String write (Date date, int noHoldRecord, int noTranRecord) {
		StringBuilder b = new StringBuilder();
		b.append(FileEndRecord)
			.append(formater.format(date))
				.append(StringUtils.leftPad(""+noHoldRecord, 8,'0'))
				.append(StringUtils.leftPad(""+noTranRecord, 8,'0'))
				.append(StringUtils.rightPad("", 502,' '))				
			;
				
		return b.toString();
		
	}
	
	public static void main(String[] args) {
		BTFWriteFooter f = new BTFWriteFooter();
		System.out.println(f.write(new Date (), 0, 12));
	}

}
