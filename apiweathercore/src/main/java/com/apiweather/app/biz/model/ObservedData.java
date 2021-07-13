package com.apiweather.app.biz.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(value = "ObservedData")
public class ObservedData {
	@Id
	private Long idObserver;
	
	private Date date;
	
	private double value;
	
	private Station station;
	
	private String type;

	
}
