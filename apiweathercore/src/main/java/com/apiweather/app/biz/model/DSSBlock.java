/**
 * 
 */
package com.apiweather.app.biz.model;

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
	
	private String units;
	
	private String type;
	
	private DSSBlockData[] dssBlockDatas;
	
	

}
