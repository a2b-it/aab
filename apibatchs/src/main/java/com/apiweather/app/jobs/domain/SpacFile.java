package com.apiweather.app.jobs.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpacFile {
	private Integer nligne;
	private Date date;
	private String stationName;
	private String type;
	private double valeur;
}
