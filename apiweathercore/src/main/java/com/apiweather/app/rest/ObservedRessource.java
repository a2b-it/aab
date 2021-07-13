package com.apiweather.app.rest;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.model.SpacObservedData;
import com.apiweather.app.biz.repo.ObservedDataRepository;
import com.apiweather.app.biz.repo.SpacObservedDataRepository;
import com.apiweather.app.tools.exception.EntityNotFoundException;




@RestController
@RequestMapping(value = "/observed")
public class ObservedRessource extends AbstractCommonRessource<ObservedDataRepository, ObservedData, Long> {
	
	@Autowired
	SpacObservedDataRepository spacObservedDataRepository;
	
	public ObservedRessource(ObservedDataRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
	@PostMapping("/spac/saveall")
	@ResponseBody
	public ResponseEntity<SpacObservedData> saveAllObservation(@RequestBody(required = true) List<SpacObservedData> obs) throws EntityNotFoundException {		
		List<SpacObservedData> list = spacObservedDataRepository.saveAll(obs);
		return new ResponseEntity(list,HttpStatus.OK);
	}
	
}
