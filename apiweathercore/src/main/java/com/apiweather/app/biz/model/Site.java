package com.apiweather.app.biz.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class Site {
	@Transient
	public static final String SEQUENCE_NAME = "site_sequence";
	@Id
	private Long siteId;
	
	private String name;
		
	private float[][] area;
	
	private float[] geocenter;
	
	private float[][] limits;
	
	private String shapeFile;
	
	private String clientName;
	
	private String spokeperson;

	private List<Station> stations;
	
	private List<Model> models;
	
	
	
}
