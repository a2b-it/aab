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
public class User {
	@Transient
	public static final String SEQUENCE_NAME = "user_sequence";
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
