package com.apiweather.app.dss;

import hec.heclib.dss.HecDataManager;

public interface DSSFileBuilder {
	
	public HecDataManager manager ();
	
	public void setLogFile (String path);
	
	public void setDefaultDSSFileName (String path);
	
	
	
	
}
