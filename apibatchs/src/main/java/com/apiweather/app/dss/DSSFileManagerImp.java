package com.apiweather.app.dss;

import hec.heclib.dss.DSSErrorMessage;
import hec.heclib.dss.HecDataManager;


public class DSSFileManagerImp implements DSSFileManager {
	
	
	private HecDataManager dataManager;
	
	private DssBlocBody bodyBloc;
	
	private DssBlocHeader headerBloc;
	
	private String dssFilePath;
	
	@Override
	public void init(String dssFilePath) {
		HecDataManager dataManager = new HecDataManager();
		this.dataManager = dataManager;
		this.dssFilePath = dssFilePath; 
		int status = headerBloc.init(DssBlocHeader.TYPE_FILE.REGULAR_SERIES, dssFilePath);
		if (status<0) {
			logStatus();
		}
		status = bodyBloc.init(dssFilePath);
		if (status<0) {
			logStatus();
		}
	}

	
	
	
	
	

	@Override
	public void logStatus() {
		// This means the open failed; could be lots of reasons, such
		// as a permission issue, a bad DSS file name, etc. You cannot continue.
		DSSErrorMessage error = dataManager.getLastError();
		// return proecessError(error); It is recommended that you write a function to handle errors
		error.printMessage();
		return;

	}

}
