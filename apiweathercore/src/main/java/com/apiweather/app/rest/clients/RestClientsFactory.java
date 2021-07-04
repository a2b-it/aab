package com.apiweather.app.rest.clients;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestClientsFactory {
	
	RestTemplate airflowClient ;
	
	@Autowired
	public static RestTemplate createAirFlowClient(RestTemplateBuilder builder)  {
	
		return builder.basicAuthentication("user", "password")
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)). build();	
	}
	
	
	@Autowired
	public static RestTemplate createWeatherClient(RestTemplateBuilder builder)  {
	
		return  builder.basicAuthentication("user", "password")
				.setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000)). build();
	
	}
}
