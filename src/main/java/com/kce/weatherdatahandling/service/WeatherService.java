package com.kce.weatherdatahandling.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.kce.weatherdatahandling.entity.WeatherData;

public interface WeatherService {

    void addDetails(MultipartFile file);

  
    WeatherData getById(String id);

    Page<WeatherData> getAllWeather(int page, int size);

    List<WeatherData> getByMonth(String month);

    Map<Integer, Map<String, Double>> getMonthlyStats(int year);
}