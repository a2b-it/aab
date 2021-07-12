package com.apiweather.app.dss;

import java.io.File;
import java.util.List;

import com.apiweather.app.dss.DssBlocHeader.TYPE_FILE;
import com.apiweather.app.excep.DSSBuildingException;

import hec.heclib.dss.DSSErrorMessage;
import hec.heclib.dss.HecDataManager;


public class DSSFileBuilderImp implements DSSFileBuilder {
	
	
	private HecDataManager dataManager;
	
	private DssBlocBody bodyBloc;
	
	private DssBlocHeader headerBloc;
	
	private String dssFilePath;
	
	
	
	@Override
	public void init(String dssFilePath) {
		HecDataManager dataManager = new HecDataManager();
		this.dataManager = dataManager;
		this.dssFilePath = dssFilePath; 
		
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







	@Override
	public DSSFileBuilder create(TYPE_FILE type, String... pathParts) {
		int status = headerBloc.init(type, dssFilePath);
		if (status<0) {
			logStatus();
		}
		headerBloc.setPathPartA(pathParts[0]);
		headerBloc.setPathPartB(pathParts[1]);
		headerBloc.setPathPartC(pathParts[2]);
		headerBloc.setPathPartD(pathParts[3]);
		headerBloc.setPathPartE(pathParts[4]);
		headerBloc.setPathPartF(pathParts[5]);
		//
		status = bodyBloc.init(headerBloc, dssFilePath);
		if (status<0) {
			logStatus();
		}		
		//			
		return this;
	}







	@Override
	public DSSFileBuilder appendData(double[] data, String units, String type, int interval) {
		try {
			bodyBloc.addData(data, units, type, interval);
			headerBloc.end();
		}catch (Exception e) {
			e.printStackTrace();
			headerBloc.close();
		}
		
		return this;
	}







	@Override
	public DSSFileBuilder appendData(List<Object> objs, String units, String type, int interval) throws DSSBuildingException {
		throw new DSSBuildingException("Method not supported");		
	}







	@Override
	public File build() {		
		return new File(dssFilePath);
	}







	@Override
	public int close() {
		dataManager.closeAndClear();
		return 0;
	}

}
