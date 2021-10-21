/**
 * 
 */
package com.apiweather.app.biz.services;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiweather.app.biz.model.DSSBlock;
import com.apiweather.app.biz.model.Output;
import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.repo.OutputRepository;
import com.apiweather.app.biz.repo.SiteRepository;
import com.apiweather.app.cfg.SequenceGeneratorService;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@Service
@Slf4j
public class ServiceDSSOutput {
	
	@Autowired
	SequenceGeneratorService sequenceGeneratorService;
	
	@Autowired
	private OutputRepository outputRepository;
	
	@Autowired
	private ServiceStation serviceStation;
	
	@Transactional
	public Output createNewOutputDssFile (String name, DSSBlock[] blocks) throws EntityNotFoundException, BusinessException {
		Station station = serviceStation.findByStationName(name);				
		Long idOutput = sequenceGeneratorService.generateSequence(Output.SEQUENCE_NAME);
		
		//TODO validation and check block dss 
		Output output = new Output();
		output.setIdStation(station.getStationId());
		output.setDssBlocks(blocks);
		output.setDate(Calendar.getInstance().getTime());
		output.setIdOutput(idOutput);
		output.setImagesPaths(null);		
		outputRepository.save(output);
				
		return output;
	}
	
}
