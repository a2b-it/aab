package com.apiweather.app.biz.model;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {
	
	@Id
	private Long weatherId;
}
