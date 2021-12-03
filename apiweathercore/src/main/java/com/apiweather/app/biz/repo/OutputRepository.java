package com.apiweather.app.biz.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.apiweather.app.biz.model.Output;

public interface OutputRepository extends MongoRepository<Output, Long> {
	
	
	@Query("{\"filename\":?0,\"idStation\":?1}")
	public List<Output> findOutputByfilenameAndStation (String filename, Long idStation);
	
	
	public List<Object> findByFilename (String filename);
	
	@Aggregation(pipeline = {"{'$match':{'stationId': ?0 }}","{$group: { '_id': '', time: {$max: $time }}}"} )
	public List <Output> findByMaxValue (long maxValue );
}
