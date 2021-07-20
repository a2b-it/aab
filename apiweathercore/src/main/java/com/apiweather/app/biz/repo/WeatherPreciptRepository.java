package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.WeatherPrecipt;

public interface WeatherPreciptRepository extends MongoRepository<WeatherPrecipt, Long> {
	
	
	@Aggregation(pipeline = {"{'$match':{'stationId': ?0 }}","{$group: { '_id': '', time: {$max: $time }}}"} )
	public WeatherPrecipt getMaxElementsByTime (long idStation);
	//@Aggregation(pipeline = {"{ '$group': { '_id' : '$lastname', names : { $addToSet : '$?0' } } }", "{ '$sort' : { 'lastname' : -1 } }"})
	 
}
