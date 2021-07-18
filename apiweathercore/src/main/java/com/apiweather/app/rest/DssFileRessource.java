/**
 * 
 */
package com.apiweather.app.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author a.bouabidi
 *
 */

@RequestMapping(value = "/dssfile")
@RestController
@Validated
public class DssFileRessource {
	
	
	
	@GetMapping("/list")
	public ResponseEntity<String> listAllData(String criteria) {
		
		
		return new ResponseEntity<String>("OK Alert",HttpStatus.OK);
	}

}
