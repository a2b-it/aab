/**
 * 
 */
package com.apiweather.app.biz.services;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.model.Weather;
import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.biz.model.weather.AgroForcast;
import com.apiweather.app.biz.model.weather.AgroWeather;
import com.apiweather.app.biz.repo.AgroWeatherRepository;
import com.apiweather.app.biz.repo.WeatherPreciptRepository;
import com.apiweather.app.biz.repo.WeatherRepository;
import com.apiweather.app.cfg.SequenceGeneratorService;
import com.apiweather.app.rest.clients.WeatherApiCaller;
import com.apiweather.app.rest.dto.WeatherPreciptByDayDTO;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;
import com.apiweather.app.tools.rest.ModelMapper;

import lombok.extern.slf4j.Slf4j;
/**
 * @author a.bouabidi
 *
 */
@Service
@Slf4j
public class ServiceWeather {
	
	private int interval_between_request=60*60;
	
	private Calendar cal = Calendar.getInstance();
	
	@Autowired
	private WeatherApiCaller weatherApiCallerImp;
	
	@Autowired
	private ServiceStation serviceStation;
	
	@Autowired
	private AgroWeatherRepository agroWeatherRepository;
	
	@Autowired
	private WeatherRepository weatherRepository;
	
	@Autowired
	ModelMapper<WeatherPrecipt, AgroWeather> weatherPreciptMapper = null;
	@Autowired
	SequenceGeneratorService sequenceGeneratorService;
	
	@Autowired
	private MongoOperations mongoOperations;
	/**
	 * skip if last saved data is less than 60 mn older  	
	 * @param idstation
	 * @return
	 * @throws EntityNotFoundException
	 * @throws BusinessException
	 */
	public AgroWeather addWeatherEntryForStation (long idstation) throws EntityNotFoundException, BusinessException {
		/*WeatherPrecipt maxWp = weatherPreciptRepository.getMaxElementsByTime(idstation);
		log.debug(" current time millis"+cal.getTimeInMillis());
		log.debug(" max db time millis"+ maxWp.getTime().getTime());
		if((cal.getTimeInMillis() - maxWp.getTime().getTime())/1000 <interval_between_request) {
			return null;
		}*/
		//
		AgroWeather  fs = requestWeatherForStation (idstation);
		//TODO check if fs is not null or throw exception
		fs = agroWeatherRepository.save(fs);		
		//
		saveWeatherPrecipt(idstation, fs);
		//
		/*
		 * int hour = cal.get(Calendar.HOUR_OF_DAY);
		int year = cal.get(Calendar.YEAR);
		int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		 * Aggregation aggregation 
		  = Aggregation.newAggregation(
				  project().and(DateOperators.Hour.hour("$time")).as("HourOfTime")	
				  .and(DateOperators.DayOfYear.dayOfYear("$time")).as("DayOfYearTime")
				  .and(DateOperators.Year.year("$time")).as("YearTime")
				  .and("$stationId").as("station")
				  , match(Criteria.where("HourOfTime").is(hour)
						  .and("DayOfYearTime").is(dayOfYear)
						  .and("YearTime").is(year)
						  .and("station").is(idstation)
						  )						  			
				  ,count().as("total")
				  );		
		//
		AggregationResults<Output> result =  mongoTemplate.aggregate(aggregation, WeatherPrecipt.class,Output.class);
		int n = result.getMappedResults().get(0).total;
		*/
		return fs;
	}
	
	@Transactional
	private Weather saveWeatherPrecipt(Long stationId,AgroWeather aw ) throws BusinessException {
		WeatherPrecipt wp= weatherPreciptMapper.mapDtoToModel(aw);
		wp.setWeatherPreciptId(sequenceGeneratorService.generateSequence(WeatherPrecipt.SEQUENCE_NAME));
		//
		Weather wt = weatherRepository.findByStationId(stationId);
		if (wt ==null) {
			Long id = sequenceGeneratorService.generateSequence(Weather.SEQUENCE_NAME);
			wt = new Weather ();
			wt.setWeatherId(id);
			wt.setStationId(stationId);
			wt.setPrecipts(new WeatherPrecipt[] {wp});
			wt = saveNewItem(wt);
		}else {
			wt = updateItem(wt, wp);
		}
		
		return wt;
	}
	
	private Weather saveNewItem(Weather wt) {		
		return weatherRepository.save(wt);
	}
	
	private Weather updateItem(Weather wt, WeatherPrecipt wp) {
		Query query = query(
				  where("_id").is(wt.getWeatherId()));
		Update update = new Update().addToSet("precipts", wp);								
		Weather r = mongoOperations.findAndModify(query, update,new FindAndModifyOptions().returnNew(true),Weather.class);
		return r;
	}
	
	public AgroWeather requestWeatherForStation (long idstation) throws EntityNotFoundException {
		Station rs = serviceStation.findByStationId(idstation);
		if (rs == null) throw new EntityNotFoundException ("Station was not found for parameters {id="+idstation+"}");
		AgroWeather fs= weatherApiCallerImp.getCurrentWeatherByLatAndLong(rs.getLon(), rs.getLat());
		fs.setStationId(idstation);
		return fs;
	}
	
	public List<AgroForcast> requestWeatherForcastForStation (long idstation) throws EntityNotFoundException {
		Station rs = serviceStation.findByStationId(idstation);
		if (rs == null) throw new EntityNotFoundException ("Station was not found for parameters {id="+idstation+"}");
		List<AgroForcast>  fs= weatherApiCallerImp.getForecastByLatLong(rs.getLon(), rs.getLat());
		//fs.setStationId(idstation);
		return fs;
	}
	
	public List<WeatherPreciptByDayDTO> weatherPreciptForStation (long idstation) throws EntityNotFoundException {
		List<WeatherPreciptByDayDTO> rs = weatherRepository.getWeatherPreciptByStationIdGroupByDate(idstation);
		if (rs.isEmpty()) throw new EntityNotFoundException ("No weither found for parameters {station="+idstation+"}");
		
		return rs;
	}
}

