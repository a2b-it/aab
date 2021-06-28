package com.apiweather.app.biz.model;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	
	@Id
	private Long idUser;
	private String email;
	private String password;
	private String lastName;
	private String firstName;
	private String phone;
	private String title;
	private String function;
	private String entity;
	private String country;
	private String region;
	private String clientAffiliation;
	private String isResponsible;
}
