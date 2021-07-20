package com.apiweather.app.rest.clients;

import java.util.List;

import com.apiweather.app.biz.model.weather.AgroWeather;
import com.apiweather.app.biz.model.weather.AgroForcast;

public interface WeatherApiCaller {

	
	public List<AgroForcast> getForecastByLatLong (double lon, double lat);
	
	public AgroWeather getCurrentWeatherByLatAndLong(double lon, double lat);
}
