/**
 * 
 */
package com.apiweather.app.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.apiweather.app.biz.model.weather.AgroWeather;
import com.apiweather.app.biz.services.ServiceWeather;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;

/**
 * @author a.bouabidi
 *
 */
@SpringBootTest
public class ServiceWeatherTest {
	
	@Autowired
	private ServiceWeather serviceWeather;
	
	
	@Test
	public void test_addWeatherEntryForStation() {
		try {
			AgroWeather  a =  serviceWeather.addWeatherEntryForStation(2);
		} catch (EntityNotFoundException | BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void test_timestampconversion() {
		long n = 1626407904L;
		Date d = new Date(n*1000);
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HHmmss");
		System.out.println("la date "+f.format(d));
		System.out.println();
		
	}
}
