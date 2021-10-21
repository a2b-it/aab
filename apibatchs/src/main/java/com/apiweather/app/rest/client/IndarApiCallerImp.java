package com.apiweather.app.rest.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.jobs.domain.SpacFile;


/**
 * @author a.bouabidi
 *
 */
@Component
public class IndarApiCallerImp implements IndarApiCaller {

	
	@Autowired
	RestClientsFactory clientfactory;
	
	
	@Override
	public List<SpacFile> saveAllObservation(List<SpacFile> liste) {
		String url = "observed/spac/saveall";
		String rootUrl = clientfactory.getApiUrl();
						
		RestTemplate client = clientfactory.createAirFlowClient();
		
		SpacFile[] sf =(SpacFile[]) liste.toArray();

		ResponseEntity r = client.postForEntity(rootUrl+url,sf,SpacFile[].class);
		if (r.getStatusCodeValue() == 200) {
			return (Arrays.asList((SpacFile[])r.getBody()));
		}
		
		return null;
	}


	@Override
	public List<DSSBlock> retreiveStationDssData (String name) {
		String url = "dssfile/data/";
		String rootUrl = clientfactory.getApiUrl();
						
		RestTemplate client = clientfactory.createAirFlowClient();
		
		ResponseEntity r = client.getForEntity(rootUrl+url+name,DSSBlock[].class);
		if (r.getStatusCodeValue() == 200) {
			return (Arrays.asList((DSSBlock[])r.getBody()));
		}
		
		return null;
	}


	@Override
	public List<DSSBlock> saveAllDssData(DSSBlock[] blocks) {
		String url = "dssfile/data/";
		String rootUrl = clientfactory.getApiUrl();
		RestTemplate client = clientfactory.createAirFlowClient();
		
		ResponseEntity r = client.postForEntity(rootUrl+url, client, DSSBlock[].class);
		if (r.getStatusCodeValue() == 200) {
			return (Arrays.asList((DSSBlock[])r.getBody()));
		}
		
		return null;
	}


	

}

