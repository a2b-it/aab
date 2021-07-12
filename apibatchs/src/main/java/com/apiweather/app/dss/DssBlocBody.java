package com.apiweather.app.dss;

public interface DssBlocBody  {

	public int init (DssBlocHeader header, String dssFilePath) ;

	public int addData (double[] values, String units, String type, int interval);

}