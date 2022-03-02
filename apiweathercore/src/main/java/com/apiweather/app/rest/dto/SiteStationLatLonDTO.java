/**
 * 
 */
package com.apiweather.app.rest.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class SiteStationLatLonDTO {

	private double lat;
	
	private double lon;
	
	private long id;
}
