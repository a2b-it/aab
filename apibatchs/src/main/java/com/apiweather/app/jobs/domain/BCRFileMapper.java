package com.apiweather.app.jobs.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


/**
 * @author a.bouabidi
 *
 */
public class BCRFileMapper implements FieldSetMapper<BCRFile>{
	
	private String filename = null;
	
	private Date date;
	
	private int hour;

	public BCRFileMapper(String filename) throws ParseException {
		super();
		//BCR_20201101_0000_20201101_0000_20201101_0100
		this.filename = filename;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		this.date= f.parse(this.filename.substring(4, 11));
		this.hour= Integer.valueOf(this.filename.substring(40));
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, hour);
	    this.date= calendar.getTime();
	}


	@Override
	public BCRFile mapFieldSet(FieldSet fieldSet) throws BindException {
		BCRFile file = new BCRFile();
		int n = fieldSet.getFieldCount();
		file.setNligne(n);
		file.setDateFile(date);
		file.setLatitude(fieldSet.readDouble("lat"));
		file.setLongitude(fieldSet.readDouble("long"));
		file.setPluie(fieldSet.readDouble("pluie"));		
	    return file;
	}

}
