package com.apiweather.app.rest;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.CollectedData;
import com.apiweather.app.biz.model.Model;
import com.apiweather.app.biz.repo.CollectedDataRepository;
import com.apiweather.app.biz.repo.ModelRepository;




@RestController
@RequestMapping(value = "/model")
public class ModelRessource extends AbstractModelRessource<ModelRepository, Model, Long>{

	public ModelRessource(ModelRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
		
}
