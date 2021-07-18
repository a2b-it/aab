package com.apiweather.app.dss;

import hec.heclib.util.HecTime;
import hec.io.TimeSeriesContainer;
import lombok.Getter;
import lombok.Setter;


/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class TimeSerieDssBlocBodyBuilder implements DssBlocBodyBuilder {
	
	public static enum TYPE{
		INST_VAL("INST-VAL"),
		INST_CUM("INST-CUM"),
		PER_AVER("PER-AVER"),
		PER_CUM("PER-CUM");
		
		public final String value;

	    private TYPE(String value) {
	        this.value = value;
	    }
		
	}
	private String dssFilePath;
	
	private int init = 0;
	
	private TimeSerieDssBlocHeaderBuilder header;	
		
	private String units ="mm";
	private String type = TYPE.PER_CUM.value;			
	private int interval = 60*24;
	
	
	public TimeSerieDssBlocBodyBuilder() {
		super();		
	}
	
	@Override
	public int init (DssBlocHeaderBuilder header, String dssFilePath) {
		this.dssFilePath = dssFilePath;
		this.header = (TimeSerieDssBlocHeaderBuilder)header;
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
