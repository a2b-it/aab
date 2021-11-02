package com.apiweather.app.rest.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;



/**
 * @author a.bouabidi
 *
 */
@Component
public class RestClientsFactory {
	
	RestTemplateBuilder builder ;
	
	@Getter
	@Value("${indarapi.url}")
	private String apiUrl;	
	
	@Value("${indarapi.user}")
	private String user;	
	
	@Value("${indarapi.password}")
	private String password;	
	
	@Autowired
	public RestClientsFactory(RestTemplateBuilder builder) {
		super();
		this.builder = builder;
	}


	@Autowired
	public RestTemplate createApiClient()  {
		int sec = 1000;
		int min = 60*1000;
		return builder.basicAuthentication(user, password)				
				.setConnectTimeout(Duration.ofMillis(1*min))
	            .setReadTimeout(Duration.ofMillis(5*min)).rootUri(apiUrl).build();	
	}
	
	
	


	
	
}
