/**
 * 
 */
package com.apiweather.app.dto;

import com.apiweather.app.dss.model.DSSBlockData;
import com.apiweather.app.jobs.domain.WeatherPrecip;
import com.apiweather.app.tools.ModelMapper;

/**
 * @author a.bouabidi
 *
 */
public class DSSBlockDataMapper implements ModelMapper<DSSBlockData[], WeatherPrecip[]>{

	@Override
	public DSSBlockData[] mapDtoToModel(WeatherPrecip[] tw) {
		int i=0;
		DSSBlockData[] tb = new DSSBlockData[tw.length];
		for (WeatherPrecip w : tw) {
			tb[i] = new DSSBlockData();
			tb[i].setDate(w.getTime());
			tb[i].setIndex(i);
			tb[i].setValue(w.getValue());
			i++;
		}
		return tb;
	}

	@Override
	public WeatherPrecip[] mapModelToDto(DSSBlockData[] m) {
		// TODO Auto-generated method stub
		return null;
	}

}
