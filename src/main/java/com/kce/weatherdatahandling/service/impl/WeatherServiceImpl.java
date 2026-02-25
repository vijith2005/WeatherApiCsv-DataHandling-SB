package com.kce.weatherdatahandling.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

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

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String line;
            reader.readLine(); // skip header

            List<WeatherData> batch = new ArrayList<>();
            int batchSize = 1000;

            while ((line = reader.readLine()) != null) {

                String[] str = line.split(",");
                if (str.length < 15) continue;

                WeatherData data = new WeatherData();

                data.setDate(str[0]); // 19961101-11:00
                data.setConds(str[1]);
                data.setTempm(str[11]);

                batch.add(data);

                if (batch.size() == batchSize) {
                    repository.saveAll(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                repository.saveAll(batch);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV: " + e.getMessage());
        }
    }

   
    @Override
    public List<WeatherData> getByMonth(String month, int page, int size) {

        if (month.length() == 1) {
            month = "0" + month;
        }

        // Regex prefix for month: 199611...
        String regex = "^\\d{4}" + month;

        List<WeatherData> result =
                repository.findByDateRegex(regex);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No weather data found for month: " + month);
        }

        return result;
    }

  
    @Override
    public Map<Integer, Map<String, Double>> getMonthlyStats(int year) {

        String prefix = String.valueOf(year);

        List<WeatherData> yearData =
                repository.findByDateStartingWith(prefix);

        if (yearData.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No weather data found for year: " + year);
        }

        Map<Integer, List<Double>> monthMap = new HashMap<>();

        for (WeatherData w : yearData) {

            if (w.getTempm() == null) continue;

            try {
                int month = Integer.parseInt(
                        w.getDate().substring(4, 6));

                monthMap.computeIfAbsent(month,
                        k -> new ArrayList<>())
                        .add(Double.parseDouble(w.getTempm()));

            } catch (Exception ignored) {}
        }

        Map<Integer, Map<String, Double>> result =
                new HashMap<>();

        for (Map.Entry<Integer, List<Double>> entry :
                monthMap.entrySet()) {

            int month = entry.getKey();
            List<Double> temps = entry.getValue();

            Collections.sort(temps);

            double min = temps.get(0);
            double max = temps.get(temps.size() - 1);
            double median = temps.get(temps.size() / 2);

            Map<String, Double> stats =
                    new HashMap<>();

            stats.put("min", min);
            stats.put("max", max);
            stats.put("median", median);

            result.put(month, stats);
        }

        return result;
    }
}