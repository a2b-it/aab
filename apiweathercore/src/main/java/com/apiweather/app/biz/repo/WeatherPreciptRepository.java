package com.apiweather.app.biz.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.rest.dto.WeatherPreciptByDayDTO;

public interface WeatherPreciptRepository extends MongoRepository<WeatherPrecipt, Long> {
	
	
	@Aggregation(pipeline = {"{'$match':{'stationId': ?0 }}","{$group: { '_id': '', time: {$max: $time }}}"} )
	public WeatherPrecipt getMaxElementsByTime (long idStation);
	//@Aggregation(pipeline = {"{ '$group': { '_id' : '$lastname', names : { $addToSet : '$?0' } } }", "{ '$sort' : { 'lastname' : -1 } }"})
	
	List<WeatherPrecipt> findByStationId(Long stationId);
	
	//
	@Aggregation(pipeline = {"{$match:{stationId:1}}", "{$project: {yearMonthDay:{$dateToString: { format: \"%Y-%m-%d\", date: \"$time\" }},valeur:'$value'} }" , "{$group: { '_id': '$yearMonthDay', sumByDay: {$sum: '$valeur' }}}"} )	
	List<WeatherPreciptByDayDTO> getWeatherPreciptByStationIdGroupByDate(String id);
	
}
