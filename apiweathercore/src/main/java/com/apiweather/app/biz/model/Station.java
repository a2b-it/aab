package com.apiweather.app.biz.model;

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
	
}
