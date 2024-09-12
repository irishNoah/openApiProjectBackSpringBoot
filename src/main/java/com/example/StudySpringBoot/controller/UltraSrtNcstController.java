package com.example.StudySpringBoot.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.StudySpringBoot.service.UltraSrtNcstService;
// import org.springframework.web.util.DefaultUriBuilderFactory;



// https://happyzodiac.tistory.com/60
// https://colabear754.tistory.com/122
// https://velog.io/@ybw903/%EC%8A%A4%ED%94%84%EB%A7%81-%EA%B3%B5%EC%8B%9D%EB%AC%B8%EC%84%9C-%EC%9D%BD%EA%B8%B01.-Spring-MVC-5-URI-Links
@RestController
public class UltraSrtNcstController {
	@Autowired
    private UltraSrtNcstService ultraSrtNcstService;

    @GetMapping("/ultraSrtNcst")
    public String fetchWeatherData() {
        List<UltraSrtNcstService.NxNyInfo> nxNyInfoList = ultraSrtNcstService.getNxNyInfoList();
        CompletableFuture<?>[] futures = nxNyInfoList.stream()
                .map(info -> ultraSrtNcstService.fetchAndSaveUltraSrtNcstDataAsync(info))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join(); // 모든 비동기 작업이 완료될 때까지 기다림

        return "Done - Weather data fetched and saved successfully!";
    }
}
