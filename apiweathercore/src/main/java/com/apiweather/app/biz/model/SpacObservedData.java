package com.apiweather.app.biz.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("SpacObserved")
public class SpacObservedData extends ObservedData {
	
	private String categ;
	
	private String stationName;
	
}
