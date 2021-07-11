package com.apiweather.app.jobs.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class SpacFileMapper implements FieldSetMapper<SpacFile>{

	@Override
	public SpacFile mapFieldSet(FieldSet fieldSet) throws BindException {
		SpacFile file = new SpacFile();
		file.setDate(fieldSet.readDate("Date","yyyy-mm-dd HH:mm:ss"));
		file.setStationName(fieldSet.readString("Station"));
		file.setType(fieldSet.readString("Type"));
		file.setValeur(fieldSet.readDouble("Valeur"));
		
	    return file;
	}

}
