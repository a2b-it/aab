/**
 * 
 */
package com.apiweather.app.rest.dto;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apiweather.app.biz.model.Weather;
import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.biz.model.weather.AgroWeather;
import com.apiweather.app.cfg.SequenceGeneratorService;
import com.apiweather.app.tools.rest.ModelMapper;

import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Component
@Setter
public class WeatherMapper implements ModelMapper<Weather, AgroWeather>{

	
	
	@Override
	public Weather mapDtoToModel(AgroWeather m) {
		
		return null;
	}

	@Override
	public AgroWeather mapModelToDto(Weather m) {
		// TODO Auto-generated method stub
		return null;
	}

}
