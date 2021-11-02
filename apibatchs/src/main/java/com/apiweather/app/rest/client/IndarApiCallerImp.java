package com.apiweather.app.rest.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.dss.model.DSSFile;
import com.apiweather.app.excep.DSSReadingException;
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
	public List<SpacFile> saveAllObservation(List<SpacFile> liste, String station) {
		String url = "observed/spac/saveall";
		String rootUrl = clientfactory.getApiUrl();
						
		RestTemplate client = clientfactory.createApiClient();
		
		SpacFile[] sf =(SpacFile[]) liste.toArray();

		ResponseEntity r = client.postForEntity(rootUrl+url,sf,SpacFile[].class);
		if (r.getStatusCodeValue() == 200) {
			return (Arrays.asList((SpacFile[])r.getBody()));
		}
		
		return null;
	}


	@Override
	public List<DSSBlock> retreiveStationDssData (String name) {
		String url = "dssfile/wheather/station/";
		String rootUrl = clientfactory.getApiUrl();
						
		RestTemplate client = clientfactory.createApiClient();
		
		ResponseEntity r = client.getForEntity(rootUrl+url+name,DSSBlock[].class);
		if (r.getStatusCodeValue() == 200) {
			return (Arrays.asList((DSSBlock[])r.getBody()));
		}
		
		return null;
	}


	@Override
	public boolean saveAllDssData(DSSFile file, String station) throws DSSReadingException{
		if ("".equals(station) || station == null) {
			throw new DSSReadingException ("Station required");
		}
		String url = "output/"+station+"/save/";
		String rootUrl = clientfactory.getApiUrl();
		RestTemplate client = clientfactory.createApiClient();
		
		ResponseEntity r = client.postForEntity(rootUrl+url, file, DSSFile.class);
		if (r.getStatusCodeValue() == 200) {			
			return true;
		}
		
		return false;
	}


	

}

