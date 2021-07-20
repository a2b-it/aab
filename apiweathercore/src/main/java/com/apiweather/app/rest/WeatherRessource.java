package com.apiweather.app.rest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.model.Weather;
import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.biz.model.weather.AgroForcast;
import com.apiweather.app.biz.model.weather.AgroWeather;
import com.apiweather.app.biz.repo.AgroWeatherRepository;
import com.apiweather.app.biz.repo.StationRepository;
import com.apiweather.app.biz.repo.WeatherPreciptRepository;
import com.apiweather.app.biz.services.ServiceWeather;
import com.apiweather.app.rest.clients.WeatherApiCaller;
import com.apiweather.app.rest.dto.SpacDTO;
import com.apiweather.app.rest.tools.ModelMapper;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/weather")
public class WeatherRessource extends AbstractCommonRessource<WeatherPreciptRepository, Weather, Long> {
	
	@Autowired
	private ServiceWeather serviceWeather;
	
	public WeatherRessource(WeatherPreciptRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
	@GetMapping("/forcast")
	@ResponseBody
	public ResponseEntity<AgroForcast> forcastWeather(long idstation) throws EntityNotFoundException, BusinessException {	
		List<AgroForcast> fs= serviceWeather.requestWeatherForcastForStation(idstation);
		System.out.println("F============= " + fs);
		return new ResponseEntity(fs,HttpStatus.OK);
	}	
	
	@GetMapping("/current/{id}")
	@ResponseBody
	public ResponseEntity<AgroWeather> currentWeather(@Valid  @NotNull @PathVariable(name = "id") long idstation) throws EntityNotFoundException, BusinessException {		
		AgroWeather  fs = serviceWeather.requestWeatherForStation (idstation);
		return new ResponseEntity (fs,HttpStatus.OK);
	}
	
	@PostMapping("/savecurrent/{id}")
	@ResponseBody	
	public ResponseEntity<AgroWeather> saveCurrentWeather(@Valid  @NotNull @PathVariable(name = "id") long idstation) throws EntityNotFoundException, BusinessException {		
		AgroWeather fs = serviceWeather.addWeatherEntryForStation (idstation);
		return new ResponseEntity (fs,HttpStatus.OK);
	}	
	
	@GetMapping("/current/all")
	@ResponseBody
	public ResponseEntity<AgroWeather> currentWeatherAll() throws EntityNotFoundException, BusinessException {		
		/*List<Station> rs = stationRepository.findAll();
		if (rs == null || rs.isEmpty()) throw new EntityNotFoundException ("no Station was found for parameters");
		List<AgroWeather> liste = new ArrayList<AgroWeather>();
		try {
			for (int j = 0; j<rs.size();j++) {
				 Station s = rs.get(j);
				 AgroWeather fs= weatherApiCallerImp.getCurrentWeatherByLatAndLong(s.getLon(), s.getLon());
				 fs.setStationId(s.getStationId());				 
				 liste.add(fs);			 				 
			}				
		}catch (Exception e) {
			throw new BusinessException(e);
		}					*/
		return new ResponseEntity(null,HttpStatus.OK);
	}
	
	@PostMapping("/savecurrent/all")
	@ResponseBody
	@Transactional
	public ResponseEntity<AgroWeather> saveCurrentWeatherAll() throws EntityNotFoundException, BusinessException {		
	/*	List<Station> rs = stationRepository.findAll();
		if (rs == null || rs.isEmpty()) throw new EntityNotFoundException ("no Station was found for parameters");
		List<AgroWeather> liste = new ArrayList<AgroWeather>();
		try {
			for (int j = 0; j<rs.size();j++) {
				 Station s = rs.get(j);
				 AgroWeather fs= weatherApiCallerImp.getCurrentWeatherByLatAndLong(s.getLon(), s.getLon());
				 fs.setStationId(s.getStationId());				 
				 liste.add(fs);
				 agroWeatherRepository.save(fs);
			}		
			
		}catch (Exception e) {
			throw new BusinessException(e);
		}				*/	
		return new ResponseEntity(null,HttpStatus.OK);
	}
	
	
}
