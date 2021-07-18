package com.apiweather.app.rest.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.model.ObservedData.CATEG;
import com.apiweather.app.cfg.SequenceGeneratorService;
import com.apiweather.app.rest.tools.ModelMapper;

import lombok.Setter;

@Component
@Setter
public class ObservedDataMapper implements ModelMapper<ObservedData, SpacDTO> {
	
	@Autowired
	SequenceGeneratorService sequenceGeneratorService;
	
	@Override
	public ObservedData mapDtoToModel(SpacDTO t) {
		Long id = (t.getId()==null)?
		id=sequenceGeneratorService.generateSequence(ObservedData.SEQUENCE_NAME):t.getId();
		return new ObservedData(id,CATEG.SPAC,t.getDateObs(),t.getValeur(),t.getStationName(),t.getType(),t.getFilename(), null);
	}

	@Override
	public SpacDTO mapModelToDto(ObservedData m) {
		SpacDTO s = new SpacDTO();
		s.setDateObs(m.getDate());
		s.setFilename(m.getFilename());
		s.setId(m.getId());
		s.setStationName(m.getStationName());
		s.setType(m.getType());
		s.setValeur(m.getValue());
		return s;
	}

}
