package com.apiweather.app.biz.model.weather;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class WeatherCondition {
	private int id;
    private String main;
    private String description;
    private String icon;
}
