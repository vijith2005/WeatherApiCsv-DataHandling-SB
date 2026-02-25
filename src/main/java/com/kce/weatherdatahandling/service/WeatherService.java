package com.kce.weatherdatahandling.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kce.weatherdatahandling.entity.WeatherData;

public interface WeatherService {

    void addDetails(MultipartFile file);

    Map<Integer, Map<String, Double>> getMonthlyStats(int year);

    List<WeatherData> getByMonth(String month, int page, int size);
}