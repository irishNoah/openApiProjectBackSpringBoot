package com.example.StudySpringBoot.controller;

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
    	ultraSrtNcstService.fetchAndSaveUltraSrtNcstData();
        return "Weather data fetched and saved successfully!";
    }
}
