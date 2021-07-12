package com.apiweather.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepo  extends CrudRepository<T, ID>{
		
}
