package com.apiweather.app.dss;

public interface DssBlocBody  {

	public int init (String dssFilePath);

	void addData(double[] values);

}