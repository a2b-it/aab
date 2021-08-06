package com.apiweather.app.biz.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Document(value = "ObservedData")
@NoArgsConstructor
@CompoundIndex(def = "{'date': 1, 'stationName': 1,'type':1}",unique = true)
public class ObservedData {
	
	@Transient
    public static final String SEQUENCE_NAME = "observeddata_sequence";	
	
	@Id
	private Long id;

	private Long stationId;
	
	private String stationName;
	
	private CATEG categ;
	
	private Date date;
	
	private double value;
	
	private String type;
	
	private String filename;
	
	public static enum CATEG{
		SPAC
	}
	
	public ObservedData(Long id, CATEG categ, Date date, double value,String stationName, String type,
			String filename,Long stationId) {
		super();
		this.id = id;
		this.stationId = stationId;
		this.stationName = stationName;
		this.categ = categ;
		this.date = date;
		this.value = value;
		this.type = type;
		this.filename = filename;
	}
	
	
}
