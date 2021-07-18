package com.apiweather.app.biz.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rain {
	 @JsonProperty("3h") 
	 private double cum3h;
}
