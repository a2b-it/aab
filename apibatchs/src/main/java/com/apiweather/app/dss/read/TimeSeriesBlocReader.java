/**
 * 
 */
package com.apiweather.app.dss.read;

import java.util.Date;

import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.dss.model.DSSBlockData;
import com.apiweather.app.excep.DSSReadingException;

import hec.heclib.dss.HecDataManager;
import hec.heclib.dss.HecTimeSeries;
import hec.io.DataContainer;
import hec.io.TimeSeriesContainer;

/**
 * @author a.bouabidi
 *
 */
public class TimeSeriesBlocReader implements DSSBlocReader {
	
	private String path;
	
	private DSSBlock dssBlock; 
		
	private TimeSeriesContainer tsc;
	
	@Override
	public String getPath() {		
		return path;
	}

	
	@Override
	public void init(DataContainer container) throws DSSReadingException {
		
		int interval=0;
		try {
			tsc = (TimeSeriesContainer)container;
			//((HecTimeSeries)container).read(tsc, true);								
			interval = tsc.getTimeInterval();
			//
			dssBlock = new DSSBlock();
			dssBlock.setDataParam (tsc.getParameterName());
			
			this.path=tsc.getFullName();
			//TODO description			
			dssBlock.setDescription (tsc.getFullName());
			dssBlock.setName (tsc.getName());
			dssBlock.setLocation (tsc.getLocationName());
			dssBlock.setTimeInterval (String.valueOf(interval));
			
			dssBlock.setStartDate(tsc.getStartTime().toString());
			dssBlock.setUnits( tsc.getUnits());
			dssBlock.setType(tsc.getType());			
			//
			
			//
			
		}catch (Exception e) {
			throw new DSSReadingException(e);
		}			
		return ;
	}

	@Override
	public DSSBlock read() {
		int n = 0;
		DSSBlockData[] arr_data= new DSSBlockData[tsc.getNumberValues()];
		while (n<tsc.getNumberValues()) {
			DSSBlockData data = new DSSBlockData();	
			data.setDate(new Date(tsc.getHecTime(n).getTimeInMillis()));
			data.setIndex(n);
			data.setValue(tsc.getValue(n));
			arr_data[n] = data;
			n++;
		}
		dssBlock.setDssBlockDatas(arr_data);
		return dssBlock;
	}

}
