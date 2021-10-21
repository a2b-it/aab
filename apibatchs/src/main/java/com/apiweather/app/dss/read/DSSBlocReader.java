package com.apiweather.app.dss.read;

import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.excep.DSSReadingException;

import hec.io.DataContainer;



/**
 * 
 * @author a.bouabidi
 *
 */
public interface DSSBlocReader {

	public String getPath();

	public void init(DataContainer container) throws DSSReadingException;

	public DSSBlock read();

}
