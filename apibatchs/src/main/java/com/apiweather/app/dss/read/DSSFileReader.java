/**
 * 
 */
package com.apiweather.app.dss.read;


import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.excep.DSSReadingException;

/**
 * @author a.bouabidi
 *
 */
public interface DSSFileReader {
	
	public void init(String dssFilePath, TYPE_FILE type, String logFilePath)  throws DSSReadingException;
	
	public String logStatus ();
	
	//public DSSFileReader create(TYPE_FILE type, String path)  throws DSSReadingException;
	
	public DSSBlock[] read() throws DSSReadingException;
	
	public int close ();
	
	public static enum TYPE_FILE{
		REGULAR_TIME_SERIES
		
	}

}
