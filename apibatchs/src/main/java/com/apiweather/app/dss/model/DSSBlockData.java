/**
 * 
 */
package com.apiweather.app.dss.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class DSSBlockData {
	
	int index;
	Date date;
	double value;

}
