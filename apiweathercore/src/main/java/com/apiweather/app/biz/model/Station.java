package com.apiweather.app.biz.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Station {
	@Id
	private Long stationId;	
	
	private float[][] coordinates;
	
	private String name;
	
	private List<Alert> alerts;
	
	private List<ObservedData> observedDatas;
	
	private List<CollectedData> collectedDatas;
	
	private List<Weather> weathers;
	
}

