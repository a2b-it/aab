/**
 * 
 */
package com.apiweather.app.biz.services;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiweather.app.biz.model.CollectedData;
import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.biz.repo.CollectedDataRepository;
import com.apiweather.app.biz.repo.ObservedDataRepository;
import com.apiweather.app.biz.repo.SiteRepository;
import com.apiweather.app.biz.repo.WeatherPreciptRepository;
import com.apiweather.app.cfg.SequenceGeneratorService;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@Service
@Slf4j
public class ServiceStation {
	
	@Autowired
	SequenceGeneratorService sequenceGeneratorService;
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private WeatherPreciptRepository weatherPreciptRepository;
	
	@Autowired
	private ObservedDataRepository observedDataRepository;
	
	@Autowired
	private CollectedDataRepository collectedDataRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	
	public Station findByStationId(Long id) throws EntityNotFoundException{
		Site site = siteRepository.findByStationId(id);
		if(site == null ) {
			throw new EntityNotFoundException ("no site was found for parameters station id = "+ id);			
		}
		for (Station station : site.getStations()) {
			if(station.getStationId().equals(id)) return station;
		}
		return searchStationInList(site.getStations(), id, null);		
	}
	
	public Station findByStationName(String name) throws EntityNotFoundException{
		Site site = siteRepository.findByStationName(name);
		if(site == null ) {
			throw new EntityNotFoundException ("no Station was found for parameters name = "+ name);
		}		
		return searchStationInList(site.getStations(), null, name);		
	}
	
	public List<Station> findAllStation() throws EntityNotFoundException{
		List<Site> sites = siteRepository.findAll();
		List<Station> stations = new ArrayList <Station>();
		for (Site site :sites) {
			for (Station station : site.getStations()) {
				stations.add(station);
			}
		}
		
		return stations;
		
	}
	
	public Station findStationTree(Long id) throws EntityNotFoundException{
		Station station = findByStationId(id);			
		List<WeatherPrecipt> ws = weatherPreciptRepository.findByStationId(station.getStationId());
		station.setWeatherPrecipts(ws);
		List<ObservedData> lod = observedDataRepository.findByStationId(station.getStationId());
		station.setObservedDatas(lod);
		List<CollectedData> cod = collectedDataRepository.findByStationId(station.getStationId());
		station.setCollectedDatas(cod);
		return station;
		
	}
	
	@Transactional
	public Site createNewStation (Long idSite, Station station) throws EntityNotFoundException, BusinessException {
		Long idstation = (station.getStationId()!=null)?station.getStationId():sequenceGeneratorService.generateSequence(Station.SEQUENCE_NAME);
		station.setStationId(idstation);
		
		Query query = query(
				  where("_id").is(idSite));
		Update update = new Update().push("stations", station);		
		Site r = mongoOperations.findAndModify(query, update,new FindAndModifyOptions().returnNew(true),Site.class);		
		return r;
	}
	
	@Transactional
	public Site updateStation (Station station) throws EntityNotFoundException, BusinessException {
		if (station.getStationId() == null) throw new BusinessException("Station Id must not be null");
		Site site = siteRepository.findByStationId(station.getStationId());		
		Query query = query(where("_id").is(site.getSiteId()).and("stations._id").is(station.getStationId()));				
		Update update = new Update().set("stations.$", station);		
		Site r = mongoOperations.findAndModify(query, update,new FindAndModifyOptions().returnNew(true),Site.class);		
		return r;
	}
	
	
	
	
	private Station searchStationInList(List<Station> stations, Long id, String name) {
		for (Station station : stations) {
			if(id != null && station.getStationId().equals(id)) return station;
			if(name != null && station.getName().equalsIgnoreCase(name)) return station;
		}
		return null;
	}
}
