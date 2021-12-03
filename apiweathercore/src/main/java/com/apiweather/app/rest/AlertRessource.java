package com.apiweather.app.rest;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.repo.AlertRepository;
import com.apiweather.app.biz.repo.AlertRepositoryCustom;
import com.apiweather.app.tools.exception.EntityNotFoundException;

import io.swagger.v3.oas.annotations.Operation;







/**
 * @author a.bouabidi
 *
 */
@RequestMapping(value = "/alert")
@RestController
@Validated
public class AlertRessource extends AbstractCommonRessource<AlertRepository, Alert, Long> {

	@Autowired
	@Qualifier("alertRepositoryCustom")
	AlertRepositoryCustom alertRepositoryCustom;
	
	
	@Autowired
	public AlertRessource(AlertRepository repo) {
		super(repo);		
	}

	@Operation(description = "check alert service if available")
	@GetMapping("/check")
	public ResponseEntity<String> test() {		
		return new ResponseEntity<String>("OK Alert",HttpStatus.OK);
	}
	
	@Operation(description = "find alert like param")
	@PostMapping("/findLike")
	@ResponseBody
	public ResponseEntity<Alert> getElementsByExpl(@RequestBody(required = true) Alert alert) throws EntityNotFoundException {		
		List<Alert> alerts = alertRepositoryCustom.findAllLikeThis(alert);
		return new ResponseEntity(alerts,HttpStatus.OK);
	}
		
}
