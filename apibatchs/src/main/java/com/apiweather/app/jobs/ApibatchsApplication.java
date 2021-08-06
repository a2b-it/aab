package com.apiweather.app.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author a.bouabidi
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.apiweather.app"})
public class ApibatchsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApibatchsApplication.class, args);
	}
	
	
	

}
