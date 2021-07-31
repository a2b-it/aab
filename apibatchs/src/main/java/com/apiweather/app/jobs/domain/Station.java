/**
 * 
 */
package com.apiweather.app.jobs.domain;

import lombok.Getter;

/**
 * @author a.bouabidi
 *
 */

@Getter
public class Station {
	
	private Long id;	
	
	private Double lon;
	
	private Double lat;	
	
	private String name;
	
	private String location;
}
