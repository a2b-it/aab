package com.apiweather.app.biz.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Station {
	@Transient
	public static final String SEQUENCE_NAME = "weather_sequence";
	@Id
	private Long stationId;	
	
	private Double lon;
	
	private Double lat;
	
	private float[][] coordinates;
	
	private String name;
	
	private List<Alert> alerts;
	
	private List<ObservedData> observedDatas;
	
	private List<CollectedData> collectedDatas;
	
	private List<WeatherPrecipt> weatherPrecipts;
	
}

