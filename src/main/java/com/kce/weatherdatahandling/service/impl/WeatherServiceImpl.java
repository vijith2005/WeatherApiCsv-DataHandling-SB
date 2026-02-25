package com.kce.weatherdatahandling.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kce.weatherdatahandling.entity.WeatherData;
import com.kce.weatherdatahandling.repository.WeatherDataRepository;
import com.kce.weatherdatahandling.service.WeatherService;
import com.kce.weatherdatahandling.util.ResourceNotFoundException;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherDataRepository repository;

    public WeatherServiceImpl(WeatherDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addDetails(MultipartFile file) {

        List<WeatherData> weatherList = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {

                String[] str = line.split(",");

                if (str.length < 15) continue;

                WeatherData data = new WeatherData();

                data.setDate(str[0]);
                data.setConds(str[1]);
                data.setDewptm(str[2]);
                data.setFog(str[3]);
                data.setHail(str[4]);
                data.setHeatindexm(str[5]);
                data.setHum(str[6]);
                data.setPrecipm(str[7]);
                data.setPressurem(str[8]);
                data.setRain(str[9]);
                data.setSnow(str[10]);
                data.setTempm(str[11]);
                data.setThunder(str[12]);
                data.setTornado(str[13]);
                data.setVism(str[14]);

                weatherList.add(data);
            }

            repository.saveAll(weatherList);

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file: " + e.getMessage());
        }
    }

    @Override
    public WeatherData getById(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Weather data not found with id: " + id));
    }

    @Override
    public Page<WeatherData> getAllWeather(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findAll(pageable);
    }

    @Override
    public Map<Integer, Map<String, Double>> getMonthlyStats(int year) {

        List<WeatherData> filtered = repository.findAll()
                .stream()
                .filter(d -> d.getDate() != null &&
                        d.getDate().startsWith(String.valueOf(year)))
                .collect(Collectors.toList());

        Map<Integer, Map<String, Double>> result = new HashMap<>();

        for (int month = 1; month <= 12; month++) {

            String monthStr = String.format("%02d", month);

            List<Double> temps = filtered.stream()
                    .filter(d -> d.getDate().substring(5, 7).equals(monthStr))
                    .map(d -> {
                        try {
                            return Double.parseDouble(d.getTempm());
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());

            if (!temps.isEmpty()) {

                double min = temps.get(0);
                double max = temps.get(temps.size() - 1);

                double median;
                int size = temps.size();

                if (size % 2 == 0)
                    median = (temps.get(size / 2 - 1) + temps.get(size / 2)) / 2;
                else
                    median = temps.get(size / 2);

                Map<String, Double> stats = new HashMap<>();
                stats.put("min", min);
                stats.put("max", max);
                stats.put("median", median);

                result.put(month, stats);
            }
        }

        return result;
    }

    @Override
    public List<WeatherData> getByMonth(String month) {

        List<WeatherData> data = repository.findByDateContaining(month);

        if (data.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No weather data found for month: " + month);
        }

        return data;
    }
}