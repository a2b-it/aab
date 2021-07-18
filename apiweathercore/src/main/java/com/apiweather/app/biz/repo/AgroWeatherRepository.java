/**
 * 
 */
package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.apiweather.app.biz.model.weather.AgroWeather;

/**
 * @author a.bouabidi
 *
 */
@Repository
public interface AgroWeatherRepository extends MongoRepository<AgroWeather, Long>{

}
