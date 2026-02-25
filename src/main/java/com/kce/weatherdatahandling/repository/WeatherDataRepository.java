package com.kce.weatherdatahandling.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kce.weatherdatahandling.entity.WeatherData;

public interface WeatherDataRepository extends MongoRepository<WeatherData , String > {
 
	List<WeatherData> findByDate(String date);

    List<WeatherData> findByConds(String conds);

    List<WeatherData> findByTempmBetween(String min, String max);

	List<WeatherData> findByDateContaining(String month);

	
	
}
