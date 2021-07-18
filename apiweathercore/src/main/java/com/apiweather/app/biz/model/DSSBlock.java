/**
 * 
 */
package com.apiweather.app.biz.model;

import java.util.Date;

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
	
	@Getter
	@Setter
	public class DSSBlockData {
		
		int index;
		Date date;
		double value;

	}
	@Getter
	@Setter
	public class DSSFile {
		
		String filename;
		String filepath;
		String logpath;
		

	}

}
