package com.apiweather.app.rest.clients;

import java.util.List;

import com.apiweather.app.biz.model.weather.AgroWeather;
import com.apiweather.app.biz.model.weather.AgroForcast;

public interface WeatherApiCaller {

	
	public List<AgroForcast> getForecastByLatLong (long lon, long lat);
	
	public AgroWeather getCurrentWeatherByLatAndLong(long lon, long lat);
}
