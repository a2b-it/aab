package com.apiweather.app.dss;

/**
 * @author a.bouabidi
 *
 */
public interface DssBlocBodyBuilder  {

	public int init (DssBlocHeaderBuilder header, String dssFilePath) ;

	public int addData (double[] values, String units, String type, int interval);

}