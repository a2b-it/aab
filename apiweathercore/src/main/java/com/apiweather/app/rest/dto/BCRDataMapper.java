package com.apiweather.app.rest.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.model.ObservedData.CATEG;
import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.cfg.SequenceGeneratorService;
import com.apiweather.app.tools.rest.ModelMapper;

import lombok.Setter;

@Component
@Setter
public class BCRDataMapper implements ModelMapper<WeatherPrecipt, BCRFileDTO> {
	
	@Autowired
	SequenceGeneratorService sequenceGeneratorService;
	
	@Override
	public WeatherPrecipt mapDtoToModel(BCRFileDTO t) {
		Long id = (t.getId()==null)?
		id=sequenceGeneratorService.generateSequence(WeatherPrecipt.SEQUENCE_NAME):t.getId();
		WeatherPrecipt w =  new WeatherPrecipt();
		w.setTime(t.getDateFile());
		w.setWeatherPreciptId(id);
		w.setType("1h");
		w.setValue(t.getPluie());
		return w;
	}

	@Override
	public BCRFileDTO mapModelToDto(WeatherPrecipt m) {
		
		return null;
	}

}
