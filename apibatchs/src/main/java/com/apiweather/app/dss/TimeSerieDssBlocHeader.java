package com.apiweather.app.dss;

import hec.heclib.dss.HecTimeSeries;
import hec.heclib.util.HecTime;
import hec.io.TimeSeriesContainer;
import lombok.Getter;

@Getter
public class TimeSerieDssBlocHeader implements DssBlocHeader {
	
	private String path;
	
	private String partA;
	
	private String partB;
	
	private String partC;
	
	private String partD;
	
	private String partE;
	
	private String partF;
	
	private HecTime index;
		
	private TYPE_FILE type_file;
	
	private HecTimeSeries dssTimeSeriesWrite;

	@Override
	public void setPathPartA(String part) {
		this.partA=part;
		
	}

	@Override
	public void setPathPartB(String part) {
		this.partB=part;
		
	}

	@Override
	public void setPathPartC(String part) {
		this.partC=part;
		
	}

	@Override
	public void setPathPartD(String part) {
		this.partD=part;
		
	}

	@Override
	public void setPathPartE(String part) {
		this.partE=part;
	}

	@Override
	public void setPathPartF(String part) {
		this.partF=part;
		
	}

	@Override
	public String getPath() {
		StringBuilder b = new StringBuilder();
		this.path = b.append("/").append(partA)
			.append("/").append(partB)
				.append("/").append(partC)
					.append("/").append(partD)
						.append("/").append(partE)
							.append("/").append(partF)
								.append("/").toString();
		return this.path;
		
		
	}

	@Override
	public int init(TYPE_FILE type, String dssFilePath) {
		this.type_file=type;		
		this.dssTimeSeriesWrite = new HecTimeSeries();		    		   
	    return this.dssTimeSeriesWrite.setDSSFileName(dssFilePath);			
	    
	}

	public int appendData(TimeSeriesContainer tsc) {
		return dssTimeSeriesWrite.write(tsc);
		
	}
	
	public HecTime getIndex() {
		return this.index;
	}

	@Override
	public int end() {
		dssTimeSeriesWrite.done();
		return 0;
	}

	@Override
	public int close() {
		dssTimeSeriesWrite.close();		
		return 0;
	}

	

}
