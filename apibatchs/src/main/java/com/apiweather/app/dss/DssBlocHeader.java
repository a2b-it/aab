package com.apiweather.app.dss;

import java.util.Date;

import com.apiweather.app.dss.DssBlocHeader.TYPE_FILE;

public interface DssBlocHeader {
	
	public void setPathPartA (String part);
	
	public void setPathPartB (String part);
		
	public void setPathPartC (String part);	
	
	public void setPathPartD (String part);
	
	public void setPathPartE (String part);
	
	public void setPathPartF (String part);
	
	public String getPath ();
	
	public int init(TYPE_FILE type, String dssFilePath, Date start);
	
	public static enum TYPE_FILE{
		REGULAR_SERIES,
		
	}
	
	public int end ();
	
	public int close ();
	

}
