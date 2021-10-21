package com.apiweather.app.biz.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Station {
	@Transient
	public static final String SEQUENCE_NAME = "station_sequence";
	@Id
	@Indexed(unique = true)
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
	
	private String status;
	
	@Transient
	private List<ObservedData> observedDatas;
	
	@Transient
	private List<CollectedData> collectedDatas;
	
	@Transient
	private List<WeatherPrecipt> weatherPrecipts;
	
}

