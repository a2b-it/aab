/**
 * 
 */
package com.apiweather.app.jobs.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class WeatherPrecip {
	
	private Long Id;
	
	private Long stationId;	
	
	private Date time;
	
	private Double value; 
	
	private String type;
}
