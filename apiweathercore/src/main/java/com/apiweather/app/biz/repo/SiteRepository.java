package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Site;

public interface SiteRepository extends MongoRepository<Site, Long>, SiteRepositoryCustom {
	


}
