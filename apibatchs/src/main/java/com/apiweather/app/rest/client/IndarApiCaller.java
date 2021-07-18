package com.apiweather.app.rest.client;

import java.util.List;

import com.apiweather.app.dss.model.DSSBlock;
import com.apiweather.app.jobs.domain.SpacFile;


/**
 * @author a.bouabidi
 *
 */
public interface IndarApiCaller {

	
	public List<SpacFile> saveAllObservation (List<SpacFile> liste);
	
	public List<DSSBlock> getDssFileData ();
}
