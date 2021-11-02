/**
 * 
 */
package com.apiweather.app.dss.model;

import com.apiweather.app.excep.DSSBuildingException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class DSSBlock {		
	/**
	 * Project, river, or basin name
	 */
	
	private String name;
	/**
	 * Location
	 */
	private String location;
	/**
	 * Data parameter
	 */
	private String dataParam;
	/**
	 * Starting date of block, in a 9 character military format
	 */
	private String startDate;
	
	/**
	 * Time interval
	 */	
	private String timeInterval;
	
	/**
	 * Additional user-defined descriptive information
	 */
	private String description;
	
	/**
	 * "INST-VAL"(A value for a specific time)
		"INST-CUM"(A cumulative measurement, such as a precipitation mass curve)
		"PER-AVER"(An average over the previous time and this time)
		"PER-CUM"(Accumulation over the period, such as incremental precipitation)
	 */
	private String units;
	
	private String type;
	
	private DSSBlockData[] dssBlockDatas;
	
	@JsonIgnore
	public int getTimeIntervalAsInt() throws DSSBuildingException {
		switch (timeInterval) {
		case "1DAY":{}
			return 60*24;
		case "1HOUR":{}
			return 60;
		default :{
			throw new DSSBuildingException("Inappropriate Time Interval String value");			
		}			
		}
	}
	
	@JsonIgnore
	public double[] getDssBlockDataAsDouble() {
		double[] tab = new double[dssBlockDatas.length];
		for (int i=0; i<dssBlockDatas.length; i++ ) {
			tab[i]=dssBlockDatas[i].value;
					
		}
		return tab;
		
	}
	
	

}
