package com.apiweather.app.biz.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Weather;
import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.rest.dto.WeatherPreciptByDayDTO;

public interface WeatherRepository extends MongoRepository<Weather, Long> {
	
	Weather findByStationId(Long station);
	
	@Aggregation(pipeline = {"{'$match':{'stationId': ?0 }}","{$group: { '_id': '', time: {$max: $time }}}"} )
	public WeatherPrecipt getMaxElementsByTime (long idStation);
	
	@Aggregation(pipeline = {"{$match:{'stationId':?0}}","{ $unwind : '$precipts' }","{$project:{yearmonthday:{$dateToString:{ format: \"%Y-%m-%d\", date: \"$precipts.time\" }},valeur:'$precipts.value'}}","{$group:{_id:'$yearmonthday',sumByDay:{$sum:'$valeur'}}}" })
	List<WeatherPreciptByDayDTO> getWeatherPreciptByStationIdGroupByDate(Long id);
	
}
