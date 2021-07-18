/**
 * 
 */
package com.apiweather.app.jobs;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.apiweather.app.dss.model.DSSBlock;

/**
 * @author a.bouabidi
 *
 */
public class RESTInputDssStepReader implements ItemReader<DSSBlock>{

	@Override
	public DSSBlock read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		return null;
	}
		

}
