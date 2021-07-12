package com.apiweather.app.dss;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import hec.heclib.dss.HecTimeSeries;
import hec.heclib.util.HecTime;
import hec.io.TimeSeriesContainer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSerieDssBlocBody implements DssBlocBody {
	
	private String dssFilePath;
	
	private int init = 0;
	
	private TimeSerieDssBlocHeader header;	
	
	
	@Autowired
	public TimeSerieDssBlocBody(DssBlocHeader header) {
		super();
		this.header = (TimeSerieDssBlocHeader)header;
	}
	
	@Override
	public int init (String dssFilePath) {
		this.dssFilePath = dssFilePath;
		HecTimeSeries dssTimeSeriesWrite = new HecTimeSeries();		    		   
	    return dssTimeSeriesWrite.setDSSFileName(this.dssFilePath);			
	    
	}
	
	
	@Override
	public void addData (double[] values) {
		//TODO adding some checks
		String path = header.getPath();
		TimeSeriesContainer tsc = createTimeSeriesContainer(path, values, header.getIndex() );
		
		
		
	}

	
	private TimeSeriesContainer createTimeSeriesContainer( String path, double[] flows, HecTime start ) {
		//if (tsc == null) {
		TimeSeriesContainer	tsc = new TimeSeriesContainer();			
		tsc.fullName = path;
		tsc.units ="mm";
	    tsc.type = "PER-CUM";			
		tsc.interval = 60*24;
		//}		
		tsc.setStartTime(start);
		//double[] flows1 = new double[] {0.1f,2.1f,1.1f,4.1f,3.1f,6.1f,5.1f,8.1f,7.1f,9.1f};		
		int[] times = new int[flows.length] ;			
		for (int j=0 ; j<flows.length; j++) {
			 times[j] = start.value();
		     start.add(tsc.interval);
		 }
		tsc.values = flows;		
	    tsc.numberValues = tsc.numberValues+flows.length;
	    
	    return tsc;
		
	}

	


	
}
