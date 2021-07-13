package com.apiweather.app.rest.clients;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.apiweather.app.biz.model.weather.Forcast;

@Component
public class WeatherApiCallerImp implements WeatherApiCaller {

	
	@Autowired
	RestClientsFactory clientfactory;
	
	@Override
	public List<Forcast> getForecastByLatLong() {
		String url = "forecast";
		String rootUrl = clientfactory.getWeatherUrl();
						
		RestTemplate client = clientfactory.createWeatherClient();
		// appid=f69c128d95543884f31123ab8dbcfcf1&lat=7.369722&lon=12.354722
		//UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(clientfactory.getAirflowUrl() +url)
		HashMap<String , Object> maps = new HashMap<String, Object>();
		maps.put("appid", "f69c128d95543884f31123ab8dbcfcf1");
		maps.put("lat", 7.369722);
		maps.put("long", 12.354722);
		URI uri = UriComponentsBuilder.fromHttpUrl(rootUrl +url).
				queryParam("appid", "f69c128d95543884f31123ab8dbcfcf1").
				queryParam("lat", 7.369722).
				queryParam("lon", 12.354722).build().toUri();
		
		ResponseEntity r = client.getForEntity(uri, Forcast[].class);
		if (r.getStatusCodeValue() == 200) {
			return (Arrays.asList((Forcast[])r.getBody()));
		}
		System.out.println("r"+r);
		return null;
	}

}

