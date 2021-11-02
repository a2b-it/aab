/**
 * 
 */
package com.apiweather.app.biz.services;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiweather.app.biz.model.DSSBlock;
import com.apiweather.app.biz.model.DSSBlockData;
import com.apiweather.app.biz.model.DSSFile;
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
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Transactional
	public Output saveOutputDssFileBlockData (String stationName,DSSFile file) throws EntityNotFoundException, BusinessException {
		Station station = serviceStation.findByStationName(stationName);				
		boolean newItem = false;
		
		//TODO validation and check block dss 
		List<Output> outputs = outputRepository.findOutputByfilenameAndStation( file.getFilename(), station.getStationId());
		//if(outputs == null || outputs.isEmpty()) throw new EntityNotFoundException ("No output found ");
		Output output = (outputs!=null && !outputs.isEmpty())?outputs.get(0):null;
		if(output ==null) {
			output = new Output();
			Long idOutput = sequenceGeneratorService.generateSequence(Output.SEQUENCE_NAME);
			output.setIdOutput(idOutput);
			newItem =true;
		}
				
		output.setIdStation(station.getStationId());
		output.setDssBlocks(file.getBlocks());
		output.setFilename(file.getFilename());
		output.setDate(Calendar.getInstance().getTime());
		
		output.setImagesPaths(null);	
		if (newItem)
			output = outputRepository.save(output);
		else 
			for (DSSBlock block : output.getDssBlocks())
				output = appendBlocksData (block.getDssBlockDatas(), block.getName(), output.getIdOutput());
		return output;
	}
	
	private Output appendBlocksData (DSSBlockData[] datas, String blockname, Long idOutput) {		
		Query query = query(where("_id").is(idOutput).and("dssBlocks").elemMatch(where("name").is(blockname)));
				//and("dssBlocks.name").is(name));				
		Update update = new Update().addToSet("dssBlocks.$.dssBlockDatas").each(datas);
				//addToSet("dssBlocks",  output.getDssBlocks());		
		Output r = mongoOperations.findAndModify(query, update,new FindAndModifyOptions().returnNew(true),Output.class);		
		return r;
	}
	
}
