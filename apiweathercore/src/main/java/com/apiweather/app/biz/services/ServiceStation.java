/**
 * 
 */
package com.apiweather.app.biz.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiweather.app.biz.model.CollectedData;
import com.apiweather.app.biz.model.ObservedData;
import com.apiweather.app.biz.model.Station;
import com.apiweather.app.biz.model.WeatherPrecipt;
import com.apiweather.app.biz.repo.CollectedDataRepository;
import com.apiweather.app.biz.repo.ObservedDataRepository;
import com.apiweather.app.biz.repo.StationRepository;
import com.apiweather.app.biz.repo.WeatherPreciptRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@Service
@Slf4j
public class ServiceStation {
	@Autowired
	private StationRepository stationRepository;
	
	private WeatherPreciptRepository weatherPreciptRepository;
	
	private ObservedDataRepository observedDataRepository;
	
	private CollectedDataRepository collectedDataRepository;
	
	@Transactional
	private Station findStationTree(Long id){
		Optional<Station> os = stationRepository.findById(id);
		Station station = os.get();		
		List<WeatherPrecipt> ws = weatherPreciptRepository.findByStationId(station.getStationId());
		station.setWeatherPrecipts(ws);
		List<ObservedData> lod = observedDataRepository.findByStationId(station.getStationId());
		station.setObservedDatas(lod);
		List<CollectedData> cod = collectedDataRepository.findByStationId(station.getStationId());
		station.setCollectedDatas(cod);
		return station;
		
	}
}
