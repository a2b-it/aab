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
public class Output {
	@Transient
	public static final String SEQUENCE_NAME = "output_sequence";
	@Id
	private Long idOutput;
	
	private Long idStation;
	
	private String filename;
	
	private Date date;
	
	private String[] imagesPaths;
	
	private DSSBlock[] dssBlocks;
}
