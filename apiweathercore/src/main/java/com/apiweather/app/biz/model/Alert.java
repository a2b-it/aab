package com.apiweather.app.biz.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Alert {
	
	
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
