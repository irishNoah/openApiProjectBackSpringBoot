package com.example.StudySpringBoot.service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UltraSrtNcstMongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RestTemplate restTemplate;

    // MongoDB에 날씨 데이터를 저장하는 메서드
    public void saveWeatherData(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            for (JsonNode item : items) {
                WeatherData weatherData = new WeatherData();
                weatherData.setBaseDate(item.path("baseDate").asText());
                weatherData.setBaseTime(item.path("baseTime").asText());
                weatherData.setCategory(item.path("category").asText());
                weatherData.setObsValue(item.path("obsrValue").asText());
                weatherData.setNx(item.path("nx").asInt());
                weatherData.setNy(item.path("ny").asInt());

                // MongoDB에 삽입
                mongoTemplate.save(weatherData, "weatherData");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 비동기 API 호출 및 MongoDB에 데이터 저장
    @Async
    public CompletableFuture<Void> fetchAndSaveUltraSrtNcstMongoDataAsync(int nx, int ny) {
        String baseDate = getCurrentDate();
        String baseTime = getCurrentTimeUltraNcst();
        String url = createUrl(nx, ny, baseDate, baseTime);

        try {
            // API 요청 및 JSON 응답 수신
            String response = restTemplate.getForObject(URI.create(url), String.class);
            // 응답 파싱 및 MongoDB에 삽입
            saveWeatherData(response);

        } catch (Exception e) {
            System.out.println("오류 발생: nx=" + nx + ", ny=" + ny + ". 오류: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    // URL 생성
    private String createUrl(int nx, int ny, String baseDate, String baseTime) {
        String resultUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?serviceKey=UjeRRoTT0jV849PNvUg%2F0H%2BxNBxqMtejj1%2F9WiC93CmJrbGGBkyBDUA5qLEpP8e%2Bn%2B65xsp5rLQhhCWfBJ%2B78w%3D%3D"
                + "&numOfRows=50&pageNo=1"
                + "&base_date=" + baseDate
                + "&base_time=" + baseTime
                + "&nx=" + nx
                + "&ny=" + ny
                + "&dataType=JSON";

        return resultUrl;
    }

    // 날씨 데이터 모델
    public static class WeatherData {
        private String baseDate;
        private String baseTime;
        private String category;
        private String obsValue;
        private int nx;
        private int ny;

        // Getters and Setters
        public String getBaseDate() {
            return baseDate;
        }

        public void setBaseDate(String baseDate) {
            this.baseDate = baseDate;
        }

        public String getBaseTime() {
            return baseTime;
        }

        public void setBaseTime(String baseTime) {
            this.baseTime = baseTime;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getObsValue() {
            return obsValue;
        }

        public void setObsValue(String obsValue) {
            this.obsValue = obsValue;
        }

        public int getNx() {
            return nx;
        }

        public void setNx(int nx) {
            this.nx = nx;
        }

        public int getNy() {
            return ny;
        }

        public void setNy(int ny) {
            this.ny = ny;
        }
    }

    // 오늘 날짜를 YYYYMMDD 형식으로 반환하는 함수
    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return currentDate.format(formatter);
    }

    // 현재 시각을 HHMM 형식으로 반환하는 함수 (정시에 맞춰서 반환)
    private String getCurrentTimeUltraNcst() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        return String.format("%02d00", hour);
    }
}
