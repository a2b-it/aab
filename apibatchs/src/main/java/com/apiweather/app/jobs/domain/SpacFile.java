package com.apiweather.app.jobs.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;



/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class SpacFile {
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
