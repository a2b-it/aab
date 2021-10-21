package com.apiweather.app.rest;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.DSSBlock;
import com.apiweather.app.biz.model.Output;
import com.apiweather.app.biz.repo.OutputRepository;
import com.apiweather.app.biz.services.ServiceDSSOutput;
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
		// TODO Auto-generated constructor stub
	}
	
	@PostMapping("/{name}/add/")
	@ResponseBody
	public ResponseEntity<Output> addOutputDssFile (@PathVariable(name = "station") String name, @Valid  @NotNull @RequestBody DSSBlock[] blocks) throws EntityNotFoundException, BusinessException {
		Output out = serviceOutput.createNewOutputDssFile(name, blocks);
		return  new ResponseEntity(out,HttpStatus.OK);
	}
		
}
