package com.apiweather.app.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.repo.SiteRepository;




@RestController
@RequestMapping(value = "/site")
public class SiteRessource extends AbstractModelRessource<SiteRepository, Site, Long>{

	public SiteRessource(SiteRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	

		
}
