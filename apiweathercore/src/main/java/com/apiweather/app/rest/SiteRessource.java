package com.apiweather.app.rest;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.repo.SiteRepository;
import com.apiweather.app.tools.exception.EntityNotFoundException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/site")
public class SiteRessource extends AbstractCommonRessource<SiteRepository, Site, Long>{

	public SiteRessource(SiteRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/like/")
	@ResponseBody
	public ResponseEntity<Site> findElementByExemple(@Valid  @NotNull @RequestBody Site site) throws EntityNotFoundException {		
		List<Site> sites = getRepository().findAllLikeThis(site);
		return new ResponseEntity(sites,HttpStatus.OK);
	}		
	
	
	
	
		
}
