package com.apiweather.app.biz.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.ObservedData;

public interface ObservedDataRepository extends MongoRepository<ObservedData, Long> {
	
	List<ObservedData> findByStationId(Long id);

}
