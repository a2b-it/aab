package com.apiweather.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.apiweather.app.jobs.domain.SpacFile;

@Repository
public interface WeatherDataRepo  extends CrudRepository<SpacFile, Long>{
		
}
