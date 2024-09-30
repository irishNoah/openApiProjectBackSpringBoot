package com.example.StudySpringBoot.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.StudySpringBoot.service.UltraSrtNcstMongoService;
import com.example.StudySpringBoot.service.UltraSrtNcstService;

@RestController
public class UltraSrtNcstMongoController {
    
    @Autowired
    private UltraSrtNcstService ultraSrtNcstService;

    @Autowired
    private UltraSrtNcstMongoService ultraSrtNcstMongoService;

    @GetMapping("/ultraSrtNcstMongo")
    public String fetchWeatherDataToMongo() {
        List<UltraSrtNcstService.NxNyInfo> nxNyInfoList = ultraSrtNcstService.getNxNyInfoList();
        CompletableFuture<?>[] futures = nxNyInfoList.stream()
                .map(info -> ultraSrtNcstMongoService.fetchAndSaveUltraSrtNcstMongoDataAsync(info.getNx(), info.getNy()))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join(); // 모든 비동기 작업이 완료될 때까지 기다림

        return "Done - Weather data fetched and saved to MongoDB successfully11!!!";
    }
}
