package com.apiweather.app.biz.model;

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
public class Model {
	@Transient
	public static final String SEQUENCE_NAME = "model_sequence";
	@Id
	private Long modelId;
	
	private String modelName;
	
	private String type;
	
	private int priority;
	
	private Site site;
	
	private Output output;
}
