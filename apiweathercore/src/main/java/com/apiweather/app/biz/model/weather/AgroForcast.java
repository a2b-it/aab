package com.apiweather.app.biz.model.weather;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class AgroForcast {
	private Long stationId;
	private int dt;
    private List<WeatherCondition> weather;
    private Main main;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
}
