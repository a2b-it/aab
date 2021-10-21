/**
 * 
 */
package com.apiweather.app.biz.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.repo.SiteRepository;
import com.apiweather.app.tools.exception.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;


/**
 * @author a.bouabidi
 *
 */
@Service
@Slf4j
public class ServiceSite {
	
	
	@Autowired
	private SiteRepository siteRepository;
	
	public Site findSiteTree(Long siteId, String name) throws EntityNotFoundException {
		Site r = null;
		Optional<Site> site = siteRepository.findById(siteId);
		if(site.isEmpty()) {
			Site s = new Site();
			s.setName(name);
			Example e = Example.of(s);
			site = siteRepository.findOne(e);
			if(site.isEmpty()) throw new EntityNotFoundException("No Site Found");
			
		};
		r = site.get();
		//
		return r;
	}
	
}
