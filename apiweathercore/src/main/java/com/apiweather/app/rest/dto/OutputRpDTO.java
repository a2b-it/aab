/**
 * 
 */
package com.apiweather.app.rest.dto;

import java.util.Date;

import com.apiweather.app.biz.model.DSSBlock;
import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class OutputRpDTO {
	@JsonAlias("_id")
	private Long idOutput;
	
	private Long idStation;
	
	private String filename;
	
	private Date date;	
		
	private DSSBlock dssBlocks;

}
