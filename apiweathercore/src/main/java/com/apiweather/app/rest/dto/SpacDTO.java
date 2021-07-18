package com.apiweather.app.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpacDTO {
	
	private Long id;
	
	private Integer nligne;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING,
			pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonProperty("dateObs")
	private Date dateObs;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING,
			pattern = "dd/MM/yyyy HH:mm:ss")	
	@JsonProperty("station")
	private String stationName;
	
	private String filename;
	
	private String type;
	
	private double valeur;
	
	
}
