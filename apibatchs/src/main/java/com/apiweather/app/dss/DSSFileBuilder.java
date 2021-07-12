package com.apiweather.app.dss;

import java.io.File;
import java.util.List;

import com.apiweather.app.dss.DssBlocHeader.TYPE_FILE;
import com.apiweather.app.excep.DSSBuildingException;

public interface DSSFileBuilder {
		

	public void init(String dssFilePath);
	
	public void logStatus ();
	
	public DSSFileBuilder create(TYPE_FILE type, String ... pathParts)  throws DSSBuildingException;
	
	public DSSFileBuilder appendData(double[] data, String units, String type, int interval)  throws DSSBuildingException;
	
	public DSSFileBuilder appendData(List<Object> objs, String units, String type, int interval)  throws DSSBuildingException;
	
	public File build()  throws DSSBuildingException;
	
	public int close ();
}
