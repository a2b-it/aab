/**
 * 
 */
package com.apiweather.app.dss.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class DSSFile {
	
	private String filename;
	private String filepath;
	private String logpath;
	private DSSBlock[] blocks;

}
