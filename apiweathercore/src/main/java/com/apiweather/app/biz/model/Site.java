package com.apiweather.app.biz.model;

import java.io.File;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Site {
	
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
