/**
 * 
 */
package com.apiweather.app.rest.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.model.AlertStatus;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.rest.ModelMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author a.bouabidi
 *
 */
public class OutputRpToAlertMapper implements ModelMapper<Alert, OutputRpDTO> {
	
	private static String START_DATE="START_DATE";
	private static String UNITS="UNITS";
	private static String TYPE="TYPE";
	private static String DATA="DATA";
	
	

	@Override
	public Alert mapDtoToModel(OutputRpDTO m) throws BusinessException {
		ObjectMapper jsonMapper = new ObjectMapper ();
		SimpleDateFormat f = new SimpleDateFormat("");
		Alert alert = new Alert();
		alert.setDate(new Date());
		alert.setStatus(AlertStatus.confirmed);
		alert.setLevel(1);
		Map<String, String> elements = new HashMap<String, String>();
		elements.put(START_DATE, m.getDssBlocks().getStartDate());
		elements.put(TYPE, m.getDssBlocks().getType());
		elements.put(UNITS, m.getDssBlocks().getUnits());
		String details;
		try {			
			elements.put(DATA, jsonMapper.writeValueAsString(m.getDssBlocks().getDssBlockDatas()));
			details = jsonMapper.writeValueAsString(elements);
			
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getMessage());
		}
		alert.setDetails(details);
		
		return alert;
	}

	@Override
	public OutputRpDTO mapModelToDto(Alert m) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	


}
