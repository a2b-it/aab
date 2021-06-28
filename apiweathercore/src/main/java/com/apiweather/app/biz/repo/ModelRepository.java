package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Model;

public interface ModelRepository extends MongoRepository<Model, Long> {
	


}
