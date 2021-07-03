package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Alert;

public interface AlertRepository extends MongoRepository<Alert, Long>, AlertRepositoryCustom {
	


}
