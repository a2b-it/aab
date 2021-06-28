package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.CollectedData;

public interface CollectedDataRepository extends MongoRepository<CollectedData, Long> {
	


}
