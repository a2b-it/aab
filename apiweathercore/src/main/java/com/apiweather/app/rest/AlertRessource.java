package com.apiweather.app.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.repo.AlertRepository;





@RequestMapping(value = "/alert")
@RestController
@Validated
public class AlertRessource extends AbstractModelRessource<AlertRepository, Alert, Long> {

	
	@Autowired
	public AlertRessource(AlertRepository repo) {
		super(repo);		
	}
				
	@GetMapping("/check")
	public ResponseEntity<String> test() {		
		return new ResponseEntity<String>("OK Alert",HttpStatus.OK);
	}
	
	/*
	@GetMapping("/list")
	public ResponseEntity<List<Site>> list(Boolean inc) {		
		List<Alert> liste = repo.findAll();
		return new ResponseEntity(liste,HttpStatus.OK);
	}
	
	@GetMapping("/get")
	public ResponseEntity<Alert> getElement(Long id) {		
		Optional<Alert> s = repo.findById(id);
		return new ResponseEntity(s.get(),HttpStatus.OK);
	}
	
	
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<Alert> saveOrUpdate(@Valid @RequestBody Alert a) {		
		Alert r = repo.save(a);
		return new ResponseEntity(r,HttpStatus.OK);
	} */
		
}
