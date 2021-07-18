package com.apiweather.app.biz.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class Alert {
	@Transient
	public static final String SEQUENCE_NAME = "alert_sequence";
	
	@Id	
	private Long idalert;
	
	private Site site;
	
	@NotNull(message = "{email.notempty}")
	private Date date;
	
	private Integer level;
	
	@NotBlank
	private String details;
	
	private AlertStatus status;
	
	private Integer source;
	

}
