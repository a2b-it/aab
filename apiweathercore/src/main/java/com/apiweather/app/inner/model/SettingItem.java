package com.apiweather.app.inner.model;

import org.springframework.data.annotation.Id;

public class SettingItem {
	
	@Id
	private Long Id;
		
	private String context;
	
	private String key;
	
	private String value;
	

}
