package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.ObservedData;

public interface SpacObservedDataRepository extends MongoRepository<ObservedData, Long> {
	


}
