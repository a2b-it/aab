package com.apiweather.app.biz.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Output {
	@Id
	private Long idOutput;
	
	private Date date;
	private String path;
	private String[] files;
}
