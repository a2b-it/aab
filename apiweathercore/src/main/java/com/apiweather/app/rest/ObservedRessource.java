package com.apiweather.app.rest;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.repo.ObservedDataRepository;
import com.apiweather.app.rest.dto.SpacDTO;
import com.apiweather.app.rest.tools.ModelMapper;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;




/**
 * @author a.bouabidi
 *
 */
@RestController
@RequestMapping(value = "/observed")
public class ObservedRessource extends AbstractCommonRessource<ObservedDataRepository, ObservedData, Long> {
	
	@Autowired
	ObservedDataRepository observedDataRepository;
	
	@Autowired
	ModelMapper<ObservedData, SpacDTO> observedDataMapper = null;
	
	public ObservedRessource(ObservedDataRepository repo) {
		super(repo);
		// TODO Auto-generated constructor stub
	}
	
	
	@PostMapping("/spac/saveall")
	@ResponseBody
	public ResponseEntity<SpacDTO[]> saveAllObservation(@RequestBody(required = true) List<SpacDTO> obs) throws BusinessException {
		List<ObservedData> listToSave = new ArrayList<ObservedData>();
		for (SpacDTO l : obs) {
			listToSave.add(observedDataMapper.mapDtoToModel(l));
		}
		List<ObservedData> listSaved=null;
		try {
			listSaved = observedDataRepository.saveAll(listToSave);	
		}catch (Exception e) {
			throw new BusinessException(e);
		}		
		List<SpacDTO> lRtn = new ArrayList<SpacDTO>();
		for (ObservedData o : listSaved) {
			lRtn.add(observedDataMapper.mapModelToDto(o));			
		}
		return new ResponseEntity<SpacDTO[]>(lRtn.toArray(new SpacDTO[listSaved.size()]),HttpStatus.OK);
		
		//throw new BusinessException("Exception biziii");
		
	}
	
}
