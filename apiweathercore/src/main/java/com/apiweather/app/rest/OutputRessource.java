package com.apiweather.app.rest;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.DSSFile;
import com.apiweather.app.biz.model.Output;
import com.apiweather.app.biz.repo.OutputRepository;
import com.apiweather.app.biz.services.ServiceDSSOutput;
import com.apiweather.app.rest.dto.OutputRpDTO;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/output")
public class OutputRessource extends AbstractCommonRessource<OutputRepository, Output, Long>{
	
	@Autowired
	private ServiceDSSOutput serviceOutput;
	
	
	
	public OutputRessource(OutputRepository repo) {
		super(repo);		
	}
	
	@PostMapping("/{station}/save")
	@ResponseBody
	public ResponseEntity<Output> addOutputDssFile (@Valid  @NotNull @RequestBody DSSFile file,
			@NotNull @PathVariable(name = "station") String station) throws EntityNotFoundException, BusinessException{						
		Output output = serviceOutput.saveOutputDssFileBlockData(station, file);	
		return new ResponseEntity(output,HttpStatus.OK);
	}
	
	@PostMapping("/alert/findandcreate")
	@ResponseBody
	public ResponseEntity<Output> searchForAlertOutputDssFile (@NotNull @PathParam(value = "station") String station, @PathParam(value = "param") String param, @PathParam(value = "seuil") double seuil) throws EntityNotFoundException, BusinessException{						
		OutputRpDTO[] outputs = serviceOutput.findSuspectValues(station, param,seuil);
		return new ResponseEntity(outputs,HttpStatus.OK);
	}
		
}
