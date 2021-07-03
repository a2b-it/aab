package com.apiweather.app.biz.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObservedData {
	@Id
	private Long idObserver;
	
	private Date date;
	
	private float waterLevel;
	
	
}
