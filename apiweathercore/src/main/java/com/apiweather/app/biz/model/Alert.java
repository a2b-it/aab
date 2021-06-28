package com.apiweather.app.biz.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Alert {
	@Id	
	private String idAlert;
	
	private Site site;
	
	@NotNull(message = "{email.notempty}")
	private Date date;
	
	private int level;
	
	@NotBlank
	private String details;
	
	private AlertStatus status;
	
	private int source;
	

}
