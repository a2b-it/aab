package com.apiweather.app.biz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Getter;
import lombok.Setter;


/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class Weather {
	@Transient
	public static final String SEQUENCE_NAME = "weather_sequence";
	@Id
	private Long weatherId;
	private Long stationId;
	private WeatherPrecipt[] precipts;
	
}
