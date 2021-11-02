/**
 * 
 */
package com.apiweather.app.rest.dto;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class WeatherPreciptMapper implements ModelMapper<WeatherPrecipt, AgroWeather>{

	
	
	@Override
	public WeatherPrecipt mapDtoToModel(AgroWeather m) {
		
		WeatherPrecipt wp = new WeatherPrecipt();
		wp.setStationId(m.getStationId());
		wp.setTime(new Date(m.getDt()*1000));
		wp.setType("3h");		
		wp.setValue((m.getRain()!=null)?m.getRain().getCum3h():0d);
		//wp.setWeatherPreciptId(id);		
		wp.setAgrWtId(m.getId());
		return wp;
	}

	@Override
	public AgroWeather mapModelToDto(WeatherPrecipt m) {
		// TODO Auto-generated method stub
		return null;
	}

}
