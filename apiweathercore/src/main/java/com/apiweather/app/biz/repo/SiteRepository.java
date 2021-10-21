package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.apiweather.app.biz.model.Site;

public interface SiteRepository extends MongoRepository<Site, Long>, SiteRepositoryCustom {
	
	@Query("{\"stations\":{'$elemMatch':{\"_id\":?0}}}")
	public Site findByStationId(Long id);
	
	
	@Query("{\"stations\":{'$elemMatch':{\"name\":?0}}}")
	public Site findByStationName(String name);
}
