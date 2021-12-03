package com.apiweather.app.rest;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.model.Weather;
import com.apiweather.app.biz.model.weather.AgroForcast;
import com.apiweather.app.biz.model.weather.AgroWeather;
import com.apiweather.app.biz.repo.WeatherPreciptRepository;
import com.apiweather.app.biz.services.ServiceStation;
import com.apiweather.app.biz.services.ServiceWeather;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;

import io.swagger.v3.oas.annotations.Operation;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/weather")
public class WeatherRessource extends AbstractCommonRessource<WeatherPreciptRepository, Weather, Long> {
	
	@Autowired
	private ServiceWeather serviceWeather;
	
	@Autowired
	private ServiceStation serviceStation;
	
	public WeatherRessource(WeatherPreciptRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
	@GetMapping("/forcast")
	@ResponseBody
	public ResponseEntity<AgroForcast> forcastWeather(long idstation) throws EntityNotFoundException, BusinessException {	
		List<AgroForcast> fs= serviceWeather.requestWeatherForcastForStation(idstation);
		System.out.println("F============= " + fs);
		return new ResponseEntity (fs,HttpStatus.OK);
	}	
	
	@GetMapping("/currentAgroWeather/{id}")
	@ResponseBody
	public ResponseEntity<AgroWeather> currentWeather(@Valid  @NotNull @PathVariable(name = "id") long idstation) throws EntityNotFoundException, BusinessException {		
		AgroWeather  fs = serviceWeather.requestWeatherForStation (idstation);
		return new ResponseEntity (fs,HttpStatus.OK);
	}
	
	@PostMapping("/saveAgroWeather/{id}")
	@ResponseBody	
	public ResponseEntity<AgroWeather> saveCurrentWeather(@Valid  @NotNull @PathVariable(name = "id") long idstation) throws EntityNotFoundException, BusinessException {
		AgroWeather fs = null;
		try {
			fs = serviceWeather.addWeatherEntryForStation (idstation);	
		}catch(BusinessException e) {
			throw e;
		}		
		return new ResponseEntity (fs,HttpStatus.OK);
	}	
	
	@Operation(description = "get all weather data from cloud for all station without saving to db")
	@GetMapping("/currentAgroWeather/all")
	@ResponseBody
	public ResponseEntity<AgroWeather> currentWeatherAll() throws EntityNotFoundException, BusinessException {		
		List<Station> rs = serviceStation.findAllStation();
		if (rs == null || rs.isEmpty()) throw new EntityNotFoundException ("no Station was found for parameters");
		List<AgroWeather> liste = new ArrayList<AgroWeather>();
		try {
			for (int j = 0; j<rs.size();j++) {
				 Station s = rs.get(j);
				 AgroWeather fs = serviceWeather.requestWeatherForStation (s.getStationId());	 
				 liste.add(fs);			 				 
			}				
		}catch (Exception e) {
			throw new BusinessException(e);
		}					
		return new ResponseEntity(liste,HttpStatus.OK);
	}
	
	@Operation(description = "get all weather data from cloud for all station and save to db")
	@PostMapping("/saveAgroWeather/all")
	@ResponseBody
	@Transactional
	public ResponseEntity<AgroWeather> saveCurrentWeatherAll() throws EntityNotFoundException, BusinessException {		
		List<Station> rs = serviceStation.findAllStation();
		if (rs == null || rs.isEmpty()) throw new EntityNotFoundException ("no Station was found for parameters");
		List<AgroWeather> liste = new ArrayList<AgroWeather>();
		try {
			for (int j = 0; j<rs.size();j++) {
				 Station s = rs.get(j);
				 AgroWeather fs = serviceWeather.addWeatherEntryForStation(s.getStationId());
				 fs.setStationId(s.getStationId());				 
				 liste.add(fs);				 
			}			
		}catch (Exception e) {
			throw new BusinessException(e);
		}					
		return new ResponseEntity(liste,HttpStatus.OK);
	}
	
	
	
	
	 
}
