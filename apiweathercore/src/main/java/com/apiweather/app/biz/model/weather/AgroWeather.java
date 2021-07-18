/**
 * 
 */
package com.apiweather.app.biz.model.weather;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class AgroWeather {
	
	private Long Id;	
	private Long stationId;
	private int dt;
	@JsonProperty("weather")
	private List<WeatherCondition> weather;
	private Main main;
	private Wind wind;
	private Rain rain;
	private Snow snow;
	private Clouds clouds;

}
