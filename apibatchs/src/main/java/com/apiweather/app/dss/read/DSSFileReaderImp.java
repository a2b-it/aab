/**
 * 
 */
package com.apiweather.app.dss.read;

import org.springframework.stereotype.Component;

import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.excep.DSSReadingException;

import hec.heclib.dss.DSSErrorMessage;
import hec.heclib.dss.HecDataManager;
import hec.heclib.dss.HecDss;
import hec.io.DataContainer;
import hec.io.TimeSeriesContainer;


/**
 * @author a.bouabidi
 *
 */
@Component
public class DSSFileReaderImp implements DSSFileReader{
	
	private HecDss dssFile;
	
	private HecDataManager hecDataManager;
	
	//private DssBlocBodyBuilder bodyBloc;
	
	private DSSBlocReader headerBloc;
	
	
	

	@Override
	public void init(String dssFilePath, TYPE_FILE type, String logFilePath) throws DSSReadingException {		 	
		this.hecDataManager = (this.hecDataManager==null)?  new HecDataManager ():this.hecDataManager;
		
		int status = HecDataManager.setLogFile(logFilePath);		
		if (status != 0) {
			System.out.println("Error opening log file");
			throw new DSSReadingException("Error opening log file");			
		}
		status = this.hecDataManager.setDSSFileName(dssFilePath);
		if (status != 0) {			
			throw new DSSReadingException(logStatus ());			
		}
		try {
			this.dssFile = HecDss.open(dssFilePath);			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new DSSReadingException (e);
		}		
	}

	@Override
	public String logStatus() {
		// This means the open failed; could be lots of reasons, such
		// as a permission issue, a bad DSS file name, etc. You cannot continue.
		DSSErrorMessage error = hecDataManager.getLastError();
		// return proecessError(error); It is recommended that you write a function to handle errors
		error.printMessage();
		return error.errorMessage;
		
	}

	

	@Override
	public DSSBlock[] read() throws DSSReadingException {
		DSSBlock[] dssBlock = null;
		try {
			String[] listPath = hecDataManager.getPathnameList(true);	
			dssBlock = new DSSBlock[listPath.length];
			int i = 0;
			for (String path : listPath) {
				DataContainer dataContainer =  dssFile.get(path);	
				
				if(dataContainer instanceof TimeSeriesContainer) {
					headerBloc = new TimeSeriesBlocReader();
				}else {
					throw new DSSReadingException ("Container Type Not Impleted");
				}
				headerBloc.init(dataContainer);
				dssBlock[i] = headerBloc.read();
				i++;
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new DSSReadingException (e.getMessage());
		}
		
		return dssBlock;
	}

	@Override
	public int close() {
		
		hecDataManager.closeAndClear();
		HecDataManager.closeLogFile();
		return 0;
	}

}
