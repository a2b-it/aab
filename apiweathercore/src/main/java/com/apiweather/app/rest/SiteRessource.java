package com.apiweather.app.rest;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.model.AlertStatus;
import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.repo.SiteRepository;
import com.apiweather.app.biz.services.ServiceStation;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;


import lombok.extern.slf4j.Slf4j;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/site")
@Slf4j
public class SiteRessource {
	
	@Autowired
	private ServiceStation serviceStation;
	
	@Autowired
	private SiteRepository siteRepository;
	
	@GetMapping("/like/")
	@ResponseBody
	public ResponseEntity<Site> findElementByExemple(@Valid  @NotNull @RequestBody Site site) throws EntityNotFoundException {		
		List<Site> sites = siteRepository.findAllLikeThis(site);
		return new ResponseEntity(sites,HttpStatus.OK);
	}		
	
	@PostMapping("/{id}/station/add/")
	@ResponseBody
	public ResponseEntity<Site> addStation (@PathVariable(name = "id") Long idsite, @Valid  @NotNull @RequestBody Station station) throws EntityNotFoundException, BusinessException {
		Site site = serviceStation.createNewStation(idsite, station);
		return  new ResponseEntity(site,HttpStatus.OK);
	}
	
	
	@PostMapping("/station/update/")
	@ResponseBody
	public ResponseEntity<Site> updateStation (@Valid  @NotNull @RequestBody Station station) throws EntityNotFoundException, BusinessException {
		
		Site site = serviceStation.updateStation(station);
		return  new ResponseEntity(site,HttpStatus.OK);
	}
	
	@PostMapping("/station/{id}/alert/")
	@ResponseBody
	public ResponseEntity<Site> createAlertForStation (@PathVariable(name = "id") Long idStation,@Valid  @NotNull @RequestBody Alert alert) throws EntityNotFoundException, BusinessException {		
		Station station = serviceStation.addNewAlert(idStation, alert);
		return  new ResponseEntity(station,HttpStatus.OK);
	}
	
	@PostMapping("/station/{station}/alerts/readed")
	@ResponseBody
	public ResponseEntity<Site> updateAlertForStation (@PathVariable(name = "station") Long idstation,@NotNull @RequestBody Long [] idalerts) throws EntityNotFoundException, BusinessException {		
		List<Alert> alrts = serviceStation.setUpdateAlertStatus(idstation, idalerts,AlertStatus.readed);
		return  new ResponseEntity(alrts,HttpStatus.OK);
	}
	
	@GetMapping("/station/{id}/alerts/")
	@ResponseBody
	public ResponseEntity<List<Alert>> readAlertForStation (@PathVariable(name = "id") Long idStation) throws EntityNotFoundException, BusinessException {		
		Station station = serviceStation.findByStationId(idStation);
		return  new ResponseEntity(station.getAlerts(),HttpStatus.OK);
	}
		
}
