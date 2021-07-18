package com.apiweather.app.rest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
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
import com.apiweather.app.biz.repo.AgroWeatherRepository;
import com.apiweather.app.biz.repo.StationRepository;
import com.apiweather.app.biz.repo.WeatherRepository;
import com.apiweather.app.rest.clients.WeatherApiCaller;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/weather")
public class WeatherRessource extends AbstractCommonRessource<WeatherRepository, Weather, Long> {
	
	@Autowired
	private WeatherApiCaller weatherApiCallerImp;
	
	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private AgroWeatherRepository agroWeatherRepository;
	
	@Autowired
	private WeatherRepository weatherRepository;
	

	public WeatherRessource(WeatherRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
	@GetMapping("/forcast")
	@ResponseBody
	public ResponseEntity<AgroForcast> forcastWeather(long idstation) throws EntityNotFoundException, BusinessException {	
		Optional<Station> rs = stationRepository.findById(idstation);
		if (rs.isEmpty()) throw new EntityNotFoundException ("Station was not found for parameters {id="+idstation+"}");
		List<AgroForcast> fs= weatherApiCallerImp.getForecastByLatLong(rs.get().getLon(), rs.get().getLat());
		System.out.println("F============= " + fs);
		return new ResponseEntity(fs,HttpStatus.OK);
	}	
	
	@GetMapping("/current/{id}")
	@ResponseBody
	public ResponseEntity<AgroWeather> currentWeather(@Valid  @NotNull @PathVariable(name = "id") long idstation) throws EntityNotFoundException, BusinessException {		
		Optional<Station> rs = stationRepository.findById(idstation);
		if (rs.isEmpty()) throw new EntityNotFoundException ("Station was not found for parameters {id="+idstation+"}");
		AgroWeather fs= weatherApiCallerImp.getCurrentWeatherByLatAndLong(rs.get().getLon(), rs.get().getLat());
		fs.setStationId(idstation);
		agroWeatherRepository.save(fs);
		System.out.println("F============= " + fs);
		return new ResponseEntity (fs,HttpStatus.OK);
	}
	
	@PostMapping("/savecurrent/{id}")
	@ResponseBody
	@Transactional
	public ResponseEntity<AgroWeather> saveCurrentWeather(@Valid  @NotNull @PathVariable(name = "id") long idstation) throws EntityNotFoundException, BusinessException {		
		Optional<Station> rs = stationRepository.findById(idstation);
		if (rs.isEmpty()) throw new EntityNotFoundException ("Station was not found for parameters {id="+idstation+"}");
		AgroWeather fs= weatherApiCallerImp.getCurrentWeatherByLatAndLong(rs.get().getLon(), rs.get().getLat());
		fs.setStationId(idstation);
		agroWeatherRepository.save(fs);
		return new ResponseEntity (fs,HttpStatus.OK);
	}	
	
	@GetMapping("/current/all")
	@ResponseBody
	public ResponseEntity<AgroWeather> currentWeatherAll() throws EntityNotFoundException, BusinessException {		
		List<Station> rs = stationRepository.findAll();
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
		}					
		return new ResponseEntity(liste,HttpStatus.OK);
	}
	
	@PostMapping("/savecurrent/all")
	@ResponseBody
	@Transactional
	public ResponseEntity<AgroWeather> saveCurrentWeatherAll() throws EntityNotFoundException, BusinessException {		
		List<Station> rs = stationRepository.findAll();
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
		}					
		return new ResponseEntity(liste,HttpStatus.OK);
	}
	
	private void addStationToAgroWeather(AgroWeather fs) {
		
	}
}
