/**
 * 
 */
package com.apiweather.app.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.services.ServiceStation;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@SpringBootTest
@Slf4j
public class ServiceStationTest {

	@Autowired
	private ServiceStation serviceStation; 
	
	
	public void test_createNewStation() throws EntityNotFoundException, BusinessException, JsonMappingException, JsonProcessingException {
		String s1 = "{\"stationId\":null,\"lon\":12.354722,\"lat\":7.369722,\"name\":\"Cameroun\",\"location\":\"Cameroun station\"}",
				s2 = "{\"stationId\":null,\"lon\":-46.625290,\"lat\":-23.533773,\"name\":\"Brazil\",\"location\":\"Brazil station\"}",
						s3 = "{\"stationId\":null,\"lon\": -5.572783,\"lat\":33.923456,\"name\":\"Ennzala\",\"location\":\"Maroc station\"}";
		ObjectMapper map = new ObjectMapper();
		
		Station station1 = map.readValue(s1, Station.class);		
		Site site = serviceStation.createNewStation(10L, station1);
		log.debug("======================================");
		log.debug("Site = "+site);
		Station station2 = map.readValue(s2, Station.class);	
		site = serviceStation.createNewStation(11L, station2);
		log.debug("======================================");
		log.debug("Site = "+site);
		Station station3 = map.readValue(s3, Station.class);		
		site = serviceStation.createNewStation(11L, station3);
		log.debug("======================================");
		log.debug("Site = "+site);
	}
	
	@Test
	public void test_update() throws EntityNotFoundException, BusinessException, JsonMappingException, JsonProcessingException {
		String s = "{\"stationId\":11,\"lon\": -5.572788,\"lat\":33.923458,\"name\":\"Ennzala\",\"location\":\"Maroc station\"}";
		ObjectMapper map = new ObjectMapper();
		
		Station station1 = map.readValue(s, Station.class);		
		Site site = serviceStation.updateStation( station1);
		log.debug("======================================");
		log.debug("Site = "+site);
		
	}
	
	@Test
	public void test_addAlert() throws EntityNotFoundException, BusinessException, JsonMappingException, JsonProcessingException {
		String s = "{\"stationId\":11,\"lon\": -5.572788,\"lat\":33.923458,\"name\":\"Ennzala\",\"location\":\"Maroc station\"}";
		ObjectMapper map = new ObjectMapper();
		
		Station station1 = map.readValue(s, Station.class);		
		Site site = serviceStation.updateStation( station1);
		log.debug("======================================");
		log.debug("Site = "+site);
		
	}
	
}
