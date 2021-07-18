package com.apiweather.app.biz.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.apiweather.app.cfg.SequenceGeneratorService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Document(value = "ObservedData")
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'date': 1, 'stationName': 1,'type':1}",unique = true)
public class ObservedData {
	
	@Transient
    public static final String SEQUENCE_NAME = "observeddata_sequence";	
	
	@Id
	private Long id;
		
	private CATEG categ;
	
	private Date date;
	
	private double value;
	
	private String stationName;
	
	private String type;
	
	private String filename;
	
	@DBRef(lazy = true)
	private Station station;
	
	public static enum CATEG{
		SPAC
	}
	
	
}
