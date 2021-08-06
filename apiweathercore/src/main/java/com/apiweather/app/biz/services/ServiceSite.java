/**
 * 
 */
package com.apiweather.app.biz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apiweather.app.biz.model.Site;
import lombok.extern.slf4j.Slf4j;


/**
 * @author a.bouabidi
 *
 */
@Service
@Slf4j
public class ServiceSite {
	
	@Autowired
	private ServiceStation serviceStation;
	
	
	private Site findSiteTree() {
		
		
		return null;
	}
}
