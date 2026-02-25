package com.kce.weatherdatahandling.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kce.weatherdatahandling.entity.WeatherData;
import com.kce.weatherdatahandling.service.WeatherService;

@RestController
@RequestMapping("/api")
public class WeatherDataController {

    private final WeatherService service;

    public WeatherDataController(WeatherService service) {
        this.service = service;
    }


    @PostMapping("/upload")
    public ResponseEntity<String> addDetails(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("File is empty");
        }

        service.addDetails(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("CSV file uploaded and data saved successfully!");
    }

   
    @GetMapping("/month")
    public ResponseEntity<List<WeatherData>> getByMonth(
            @RequestParam String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                service.getByMonth(month, page, size));
    }

 
    @GetMapping("/stats/{year}")
    public ResponseEntity<Map<Integer, Map<String, Double>>> getStats(
            @PathVariable int year) {

        return ResponseEntity.ok(
                service.getMonthlyStats(year));
    }
}