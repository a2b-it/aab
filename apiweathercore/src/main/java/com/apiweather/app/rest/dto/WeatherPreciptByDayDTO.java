/**
 * 
 */
package com.apiweather.app.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class WeatherPreciptByDayDTO {
	
	
	private String id;
	
	private Double sumByDay;
	
}
