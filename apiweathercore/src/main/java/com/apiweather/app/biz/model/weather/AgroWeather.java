/**
 * 
 */
package com.apiweather.app.biz.model.weather;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class AgroWeather {
	
	@Id
	private String Id;	
	private Long stationId;
	private long dt;
	@JsonProperty("weather")
	private List<WeatherCondition> weatherCondition;
	private Main main;
	private Wind wind;
	private Rain rain;
	private Snow snow;
	private Clouds clouds;

}
