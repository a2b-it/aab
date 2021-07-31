package com.apiweather.app.biz.model;

import java.util.List;

import javax.validation.constraints.NotNull;

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
	@NotNull
	private Double lon;
	@NotNull
	private Double lat;
	
	@NotNull
	private String name;
	@NotNull
	private String location;
	
	private List<Alert> alerts;
	
	private List<ObservedData> observedDatas;
	
	private List<CollectedData> collectedDatas;
	
	private List<WeatherPrecipt> weatherPrecipts;
	
}

