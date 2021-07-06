package com.apiweather.app.rest.clients;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class RestClientsFactory {
	
	RestTemplateBuilder builder ;
	
	@Value("${airflow.url}")
	private String airflowUrl;
	
	@Value("${weather.url}")
	private String weatherUrl;
	
	
	@Autowired
	public RestClientsFactory(RestTemplateBuilder builder) {
		super();
		this.builder = builder;
	}


	@Autowired
	public RestTemplate createAirFlowClient()  {
	
		return builder.basicAuthentication("user", "password")				
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)).rootUri(airflowUrl).build();	
	}
	
	
	@Autowired
	public RestTemplate createWeatherClient()  {
	
		return builder.basicAuthentication("user", "password")				
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)).rootUri(weatherUrl).build();	
	
	}


	public String getAirflowUrl() {
		return airflowUrl;
	}


	public String getWeatherUrl() {
		return weatherUrl;
	}
	
	
}
