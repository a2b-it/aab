/**
 * 
 */
package ma.aab.btf.app.output;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.experimental.NonFinal;

/**
 * @author a.bouabidi
 *
 */
@Component
public class BTFWriteHeader {
	private File btf;	
	final String FileType="BTF";
	final SimpleDateFormat formater = new SimpleDateFormat("MMddyyyy");
	private Date date;		
	
	@NonFinal
	public String write (Date date, String fileDescription, String orginBranch,Date nextRunDate, boolean autoRetry,
			int numberRetry,boolean genOutput,String outputFileName, boolean partialEntries,
			boolean createHold){
		StringBuilder h = new StringBuilder();
	
		return h.toString();
	}
	
	
	public String write (Date date, String fileDescription, String orginBranch,Date nextRunDate, boolean autoRetry
			){
		StringBuilder h = new StringBuilder();
		h.append(FileType)
			.append(formater.format(date))
				.append(StringUtils.rightPad(fileDescription, 40,' '))
					.append(StringUtils.leftPad(orginBranch, 3,'0'))
						.append(formater.format(nextRunDate))
						.append(((autoRetry)?'Y':'N'))						
						.append(StringUtils.leftPad("1", 3,'0') ) //numberRetry
						
						.append(' ') //.append(((genOutput)?'Y':'N'))
						.append(StringUtils.rightPad(" ", 18,' ')) //outputFileName
						.append(StringUtils.rightPad(" ", 1,' ')) //((partialEntries)?'Y':'N')
						.append(StringUtils.rightPad(" ", 1,' ')) //.append(((createHold)?'Y':'N'))						
						.append(StringUtils.rightPad("", 438,' ')) // Filler	438  bytes 
						.append(StringUtils.rightPad("", 2,' '))//Filler	2  bytes
						
						;
		return h.toString();
	}
	
	public static void main(String[] args) {
		BTFWriteHeader b = new BTFWriteHeader();
		System.out.println(b.write(new Date (),"file name", "900",
				new Date (), false, 0, true, "", false, false));
	}
	
}
