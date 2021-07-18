package com.apiweather.app.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.CollectedData;
import com.apiweather.app.biz.repo.CollectedDataRepository;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/collected")
public class CollectedRessource extends AbstractCommonRessource<CollectedDataRepository, CollectedData, Long>{
	
	public CollectedRessource(CollectedDataRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}

		
}
