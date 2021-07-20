/**
 * 
 */
package com.apiweather.app.biz.model;

import java.util.Date;

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
public class WeatherPrecipt {
	@Transient
    public static final String SEQUENCE_NAME = "weatherprecipt_sequence";
	
	@Id
	private Long weatherPreciptId;
	
	private Long stationId;
	
	private String agrWtId;
	
	private Date time;
	
	private Double value; 
	
	private String type;
}
