/**
 * 
 */
package com.apiweather.app.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author a.bouabidi
 *
 */
public interface WeatherPrecipDTO {
	
	@JsonProperty("id")
	public Long getWeatherPreciptId();
	
	public Long getStationId();
	
	public Date getTime();
	
	public Double getValue();
	
	public String getType();

}
