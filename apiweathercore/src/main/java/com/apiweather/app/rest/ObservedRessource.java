package com.apiweather.app.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.repo.ObservedDataRepository;




@RestController
@RequestMapping(value = "/observed")
public class ObservedRessource extends AbstractModelRessource<ObservedDataRepository, ObservedData, Long> {

	public ObservedRessource(ObservedDataRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	

	
}
