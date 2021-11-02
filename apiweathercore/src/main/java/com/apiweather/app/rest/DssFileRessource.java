/**
 * 
 */
package com.apiweather.app.rest;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.model.DSSBlock;
import com.apiweather.app.biz.model.DSSFile;
import com.apiweather.app.biz.model.Output;
import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.services.ServiceDSSOutput;
import com.apiweather.app.biz.services.ServiceStation;
import com.apiweather.app.biz.services.ServiceWeather;
import com.apiweather.app.rest.dto.DSSBlockDataMapper;
import com.apiweather.app.rest.dto.WeatherPreciptByDayDTO;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.exception.EntityNotFoundException;

/**
 * @author a.bouabidi
 *
 */

@RequestMapping(value = "/dssfile")
@RestController
@Validated
public class DssFileRessource {
	
	@Autowired
	private ServiceWeather serviceWeather;
	
	@Autowired
	private ServiceStation serviceStation;
	
	@Autowired	
	private ServiceDSSOutput serviceDSSOutput;
	
	@GetMapping("/wheather/station/{name}")
	@ResponseBody
	public DSSBlock[] getStationWeatherPrecipt(@Valid  @NotNull @PathVariable(name = "name") String station) throws EntityNotFoundException, BusinessException{
		Station s = serviceStation.findByStationName(station);
		if(s==null)throw new EntityNotFoundException (" NO Station found with this name "+station);
		List<WeatherPreciptByDayDTO> liste = serviceWeather.weatherPreciptForStation(s.getStationId());
		DSSBlockDataMapper map = new DSSBlockDataMapper();
		WeatherPreciptByDayDTO[] tab = new WeatherPreciptByDayDTO[liste.size()];
		DSSBlock block = map.mapDtoToModel(liste.toArray(tab));
		block.setLocation(s.getLocation());
		block.setName(s.getName());
		block.setDescription(s.getName()+" HSM");
		block.setDataParam("ET");		
		block.setTimeInterval("1DAY");
		
		return new DSSBlock[] {block};
	}
	
}
