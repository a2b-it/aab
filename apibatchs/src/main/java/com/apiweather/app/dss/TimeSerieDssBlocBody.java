package com.apiweather.app.dss;

import java.util.ArrayList;
import java.util.List;

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
	

	
	private String units ="mm";
	private String type = "PER-CUM";			
	private int interval = 60*24;
	
	
	public TimeSerieDssBlocBody() {
		super();		
	}
	
	@Override
	public int init (DssBlocHeader header, String dssFilePath) {
		this.dssFilePath = dssFilePath;
		this.header = (TimeSerieDssBlocHeader)header;
//		/this.tscs = new ArrayList<TimeSeriesContainer> ();				
	    return 0;			
	    
	}
	
	
	@Override
	public int addData (double[] values, String units, String type, int interval) {
		//TODO adding some checks
		this.units = units;
		this.type = type;			
		this.interval = interval;
		String path = header.getPath();
		TimeSeriesContainer tsc = createTimeSeriesContainer(path, values, header.getIndex() );
		return header.appendData(tsc);
	}

	
	private TimeSeriesContainer createTimeSeriesContainer( String path, double[] flows, HecTime start ) {
		//if (tsc == null) {
		TimeSeriesContainer	tsc = new TimeSeriesContainer();			
		tsc.fullName = path;
		tsc.units =units;
	    tsc.type = type;			
		tsc.interval = interval;
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
