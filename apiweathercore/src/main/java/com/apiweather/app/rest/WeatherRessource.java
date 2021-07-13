package com.apiweather.app.rest;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Weather;
import com.apiweather.app.biz.model.weather.Forcast;
import com.apiweather.app.biz.repo.WeatherRepository;
import com.apiweather.app.rest.clients.WeatherApiCaller;
import com.apiweather.app.tools.exception.EntityNotFoundException;




@RestController
@RequestMapping(value = "/weather")
public class WeatherRessource extends AbstractCommonRessource<WeatherRepository, Weather, Long> {
	
	@Autowired
	WeatherApiCaller weatherApiCallerImp;

	public WeatherRessource(WeatherRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
	@GetMapping("/forcast")
	@ResponseBody
	public ResponseEntity<Forcast> getElementById() throws EntityNotFoundException {		
		List<Forcast> fs= weatherApiCallerImp.getForecastByLatLong();
		System.out.println("F============= " + fs);
		return new ResponseEntity(fs,HttpStatus.OK);
	}	
}
