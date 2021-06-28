package com.apiweather.app.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.repo.StationRepository;




@RestController
@RequestMapping(value = "/station")
public class StationRessource extends AbstractModelRessource<StationRepository, Station, Long>{

	public StationRessource(StationRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
		
}
