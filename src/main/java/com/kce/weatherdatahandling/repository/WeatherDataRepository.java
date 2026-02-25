package com.kce.weatherdatahandling.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.kce.weatherdatahandling.entity.WeatherData;

public interface WeatherDataRepository 
        extends MongoRepository<WeatherData, String> {

    
    List<WeatherData> findByDate(String date);

    List<WeatherData> findByCondsIgnoreCase(String conds);

   
    List<WeatherData> findByDateStartingWith(String prefix);

   
    @Query("{ 'date': { $regex: ?0 } }")
    List<WeatherData> findByDateRegex(String regex);
}