package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Station;

public interface StationRepository extends MongoRepository<Station, Long> {
	


}
