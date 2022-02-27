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
public class BCRFile {
 	private Long id;
 	
	private Integer nligne;
	
	private Date dateFile;
	
	private int hours;
	
	private String filename;
	
	private double latitude;
	
	private double longitude;
	
	private double pluie;

}
