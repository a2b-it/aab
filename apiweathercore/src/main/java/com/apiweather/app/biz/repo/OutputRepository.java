package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Output;

public interface OutputRepository extends MongoRepository<Output, Long> {
	


}
