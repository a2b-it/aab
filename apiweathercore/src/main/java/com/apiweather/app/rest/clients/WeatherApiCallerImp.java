package com.apiweather.app.rest.clients;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.apiweather.app.biz.model.weather.AgroForcast;
import com.apiweather.app.biz.model.weather.AgroWeather;

@Component
public class WeatherApiCallerImp implements WeatherApiCaller {

	
	@Autowired
	RestClientsFactory clientfactory;
	
	@Override
	public List<AgroForcast> getForecastByLatLong (double lon, double lat) {
		String url = "forecast";
		String rootUrl = clientfactory.getWeatherUrl();
						
		RestTemplate client = clientfactory.createWeatherClient();
		// appid=f69c128d95543884f31123ab8dbcfcf1&lat=7.369722&lon=12.354722
		//UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(clientfactory.getAirflowUrl() +url)
		/*HashMap<String , Object> maps = new HashMap<String, Object>();
		maps.put("appid", clientfactory.getWeatherApiKey());
		maps.put("lat", lat);
		maps.put("long", lon);*/
		URI uri = UriComponentsBuilder.fromHttpUrl(rootUrl +url).
				queryParam("appid", clientfactory.getWeatherApiKey()).
				queryParam("lat", lat).
				queryParam("lon", lon).build().toUri();
		
		ResponseEntity<AgroForcast[]> r = client.getForEntity(uri, AgroForcast[].class);
		if (r.getStatusCodeValue() == 200) {
			return (Arrays.asList((AgroForcast[])r.getBody()));
		}
		System.out.println("r"+r);
		return null;
	}

	@Override
	public AgroWeather getCurrentWeatherByLatAndLong (double lon, double lat) {
		String url = "";
		String rootUrl = clientfactory.getWeatherUrl();
						
		RestTemplate client = clientfactory.createWeatherClient();
		// appid=f69c128d95543884f31123ab8dbcfcf1&lat=7.369722&lon=12.354722
		//UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(clientfactory.getAirflowUrl() +url)
		/*HashMap<String , Object> maps = new HashMap<String, Object>();
		maps.put("appid", "f69c128d95543884f31123ab8dbcfcf1");
		maps.put("lat",lat);
		maps.put("long", lon);*/
		URI uri = UriComponentsBuilder.fromHttpUrl(rootUrl +url).
				queryParam("appid", clientfactory.getWeatherApiKey()).
				queryParam("lat", lat).
				queryParam("lon", lon).build().toUri();
		
		ResponseEntity< AgroWeather> r = client.getForEntity(uri, AgroWeather.class);
		if (r.getStatusCodeValue() == 200) {
			return ((AgroWeather)r.getBody());
		}		
		return null;
	}

}

