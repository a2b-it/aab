package com.apiweather.app.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Output;
import com.apiweather.app.biz.repo.OutputRepository;




@RestController
@RequestMapping(value = "/output")
public class OutputRessource extends AbstractCommonRessource<OutputRepository, Output, Long>{

	public OutputRessource(OutputRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
		
}
