package com.apiweather.app.biz.services;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.model.AlertStatus;
import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.repo.SiteRepository;
import com.apiweather.app.cfg.SequenceGeneratorService;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;
import com.mongodb.client.result.UpdateResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceAlert {
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	SequenceGeneratorService sequenceGeneratorService;
	
	
	public Station addNewAlert(@NotNull Long idStation, Alert alert) throws BusinessException, EntityNotFoundException {
		Site site = siteRepository.findByStationId(idStation);
		if (site == null) throw new BusinessException("No Site Found for station id "+ idStation);	
		Long idAlert = (alert.getIdalert()!=null)?alert.getIdalert():sequenceGeneratorService.generateSequence(Alert.SEQUENCE_NAME);
		alert.setIdalert(idAlert);
		//
		Query query = query(
				  where("_id").is(site.getSiteId()).and("stations").elemMatch(where("_id").is(idStation)));
		Update update = new Update().filterArray(where("outer._id").is(idStation)).push("stations.$[outer].alerts", alert);		
		//
		UpdateResult r = mongoOperations.updateFirst(query, update,Site.class);
		//
		if(r.getModifiedCount()>0) {
			return searchStationInList(site.getStations(),idStation,null);
		}else {
			throw new BusinessException("No Alert update occured");
		}		
	}
	
	public Station updateAllAlert(@NotNull Long idStation, Alert alert) throws BusinessException, EntityNotFoundException {
		Site site = siteRepository.findByStationId(idStation);
		if (site == null) throw new BusinessException("No Site Found for station id "+ idStation);	
		if(alert.getIdalert() == null)throw new BusinessException("ID alert must not be null ");
		//
		Query query = query(
				  where("_id").is(site.getSiteId()).and("stations").elemMatch(where("_id").is(idStation).and("alerts._id").is(alert.getIdalert())));
		Update update = new Update().filterArray(where("outer._id").is(idStation)).filterArray(where("inner._id").is(alert.getIdalert())).set("stations.$[outer].alerts.$[inner]", alert);		
		//
		UpdateResult r = mongoOperations.updateFirst(query, update,Site.class);
		//
		if(r.getModifiedCount()>0) {
			return searchStationInList(site.getStations(),idStation,null);
		}else {
			throw new BusinessException("No Alert update occured");
		}		
	}
	
	@Transactional
	public List<Alert> setUpdateAlertStatus(@NotNull Long idStation, @NotNull Long[] idAlerts, AlertStatus status) throws BusinessException, EntityNotFoundException {
		Site site = siteRepository.findByStationId(idStation);
		if (site == null) throw new BusinessException("No Site Found for station id "+ idStation);	
		if(idAlerts == null || idAlerts.length ==0 )throw new BusinessException("ID alert must not be null ");
		//
		Query query = query(
				  where("_id").is(site.getSiteId()).and("stations").elemMatch(where("_id").is(idStation).and("alerts._id").in(Arrays.asList(idAlerts))));
						  
		Update update = new Update().filterArray(where("outer._id").is(idStation)).filterArray(where("inner._id").in(Arrays.asList(idAlerts))). set("stations.$[outer].alerts.$[inner].status", status);		
		//
		UpdateResult r = mongoOperations.updateMulti(query, update,Site.class);
		//
		if(r.getModifiedCount()>0) {
			return searchStationInList(site.getStations(),idStation,null).getAlerts();
		}else {
			throw new BusinessException("No Alert update occured");
		}		
	}
	
	private Station searchStationInList(List<Station> stations, Long id, String name) {
		for (Station station : stations) {
			if(id != null && station.getStationId().equals(id)) return station;
			if(name != null && station.getName().equalsIgnoreCase(name)) return station;
		}
		return null;
	}
	
	
	
}
