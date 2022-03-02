package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.apiweather.app.biz.model.Site;
import com.apiweather.app.rest.dto.SiteStationLatLonDTO;

public interface SiteRepository extends MongoRepository<Site, Long>, SiteRepositoryCustom {
	
	@Query("{\"stations\":{'$elemMatch':{\"_id\":?0}}}")
	public Site findByStationId(Long id);
	
	
	@Query("{\"stations\":{'$elemMatch':{\"name\":?0}}}")
	public Site findByStationName(String name);
	
	
	@Aggregation(pipeline = {"{'$unwind': { path: '$stations', preserveNullAndEmptyArrays:false, includeArrayIndex: 'id'}}, { 'lat':'$stations.lat',   'lon':'$stations.lon' }"})
	public SiteStationLatLonDTO[] findAllStationlatLon();
}
