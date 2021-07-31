/**
 * 
 */
package com.apiweather.app.dss.model;

import com.apiweather.app.excep.DSSBuildingException;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class DSSBlock {
	
	private DSSFile dssFile;
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
	private String DataParam;
	/**
	 * Starting date of block, in a 9 character military format
	 */
	private String StartDate;
	
	/**
	 * Time interval
	 */	
	private String timeInterval;
	
	/**
	 * Additional user-defined descriptive information
	 */
	private String description;
	
	private String units;
	
	private String type;
	
	private DSSBlockData[] dssBlockDatas;
	
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
	
	public double[] getDssBlockDataAsDouble() {
		double[] tab = new double[dssBlockDatas.length];
		for (int i=0; i<dssBlockDatas.length; i++ ) {
			tab[i]=dssBlockDatas[i].value;
					
		}
		return tab;
		
	}

}
