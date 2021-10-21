package com.apiweather.app.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;


/**
 * @author a.bouabidi
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.apiweather.app"})
@IntegrationComponentScan(basePackages = "com.apiweather.app.flow")
@ImportResource("classpath:integration.xml")
public class ApibatchsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApibatchsApplication.class, args);
	}
	
	
	

}
