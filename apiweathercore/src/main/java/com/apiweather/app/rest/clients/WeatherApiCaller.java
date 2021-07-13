package com.apiweather.app.rest.clients;

import java.util.List;

import com.apiweather.app.biz.model.weather.Forcast;

public interface WeatherApiCaller {

	
	public List<Forcast> getForecastByLatLong();
}
