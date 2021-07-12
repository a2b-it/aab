package com.apiweather.app.dss;

import com.apiweather.app.dss.DssBlocHeader.TYPE_FILE;

import hec.heclib.util.HecTime;


public interface DssBlocHeader {
	
	public void setPathPartA (String part);
	
	public void setPathPartB (String part);
		
	public void setPathPartC (String part);	
	
	public void setPathPartD (String part);
	
	public void setPathPartE (String part);
	
	public void setPathPartF (String part);
	
	public String getPath ();
	
	public int init(TYPE_FILE type, String dssFilePath);
	
	public static enum TYPE_FILE{
		REGULAR_SERIES,
		
	}
	
	

}
