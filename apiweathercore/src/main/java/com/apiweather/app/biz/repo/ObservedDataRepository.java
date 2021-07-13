package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.SpacObservedData;

public interface ObservedDataRepository extends MongoRepository<SpacObservedData, Long> {
	


}
