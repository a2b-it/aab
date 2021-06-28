package com.apiweather.app.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Weather;
import com.apiweather.app.biz.repo.WeatherRepository;




@RestController
@RequestMapping(value = "/weather")
public class WeatherRessource extends AbstractModelRessource<WeatherRepository, Weather, Long> {

	public WeatherRessource(WeatherRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
		
}
