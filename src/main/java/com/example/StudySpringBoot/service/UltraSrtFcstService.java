package com.example.StudySpringBoot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.example.StudySpringBoot.dto.ApiResponse;
import com.example.StudySpringBoot.dto.ApiResponse.ApiItem;
//import com.example.StudySpringBoot.dto.ApiResponse;
//import com.example.StudySpringBoot.dto.ApiResponse.ApiItem;
import com.example.StudySpringBoot.entity.NxNyGetUltraSrtFcst;
import com.example.StudySpringBoot.entity.NxNyInfo;
import com.example.StudySpringBoot.repository.NxNyGetUltraSrtFcstRepository;
import com.example.StudySpringBoot.repository.NxNyInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class UltraSrtFcstService {
	@Autowired
    private NxNyGetUltraSrtFcstRepository repository;

    @Autowired
    private NxNyInfoRepository nxNyInfoRepository;  // nx_ny_info 테이블에서 nx, ny 정보를 가져오는 Repository

    private final String apiUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    private final String serviceKey = "UjeRRoTT0jV849PNvUg%2F0H%2BxNBxqMtejj1%2F9WiC93CmJrbGGBkyBDUA5qLEpP8e%2Bn%2B65xsp5rLQhhCWfBJ%2B78w%3D%3D";

    // 로깅 기능 추가
    private static final Logger logger = LoggerFactory.getLogger(UltraSrtFcstService.class);
    
    // 매 정시 20분에 호출되는 스케줄러
    // https://velog.io/@naked_thunder/RestTemplate-API%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0
    @Scheduled(cron = "0 46 * * * *")
    public void fetchAndSaveUltraSrtFcst() throws JsonMappingException, JsonProcessingException {
        logger.info("Scheduler started: fetchAndSaveUltraSrtFcst is running...");

        List<NxNyInfo> nxNyInfoList = nxNyInfoRepository.findAllByOrderByNxAscNyAsc();  
        String baseDate = getCurrentBaseDate();  
        String baseTime = getCurrentBaseTime();  

        // RestTemplate 생성 및 MessageConverters 설정: JSON 응답만 처리
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        for (NxNyInfo nxNy : nxNyInfoList) {
            String url = buildApiUrl(baseDate, baseTime, nxNy.getNx(), nxNy.getNy());
            logger.info("Get Scheduler fetchAndSaveUltraSrtFcst() URL = " + url);

//            // HttpHeaders 설정
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            // HttpEntity 생성
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            // RestTemplate의 getForObject 메소드를 사용하여 요청
//            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);
            
            // RestTemplate의 getForObject 메소드를 사용하여 요청
            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

            if (response != null && "00".equals(response.getResponse().getHeader().getResultCode())) {
                saveApiResponse(response.getResponse().getBody().getItems().getItem(), nxNy);
            }

        }

        // 이전 데이터 삭제
        deleteOldData(baseDate, baseTime);
    }

    private String buildApiUrl(String baseDate, String baseTime, String nx, String ny) {
        // ** 디코딩 : UjeRRoTT0jV849PNvUg/0H+xNBxqMtejj1/9WiC93CmJrbGGBkyBDUA5qLEpP8e+n+65xsp5rLQhhCWfBJ+78w==
        // String serviceKey = "UjeRRoTT0jV849PNvUg/0H+xNBxqMtejj1/9WiC93CmJrbGGBkyBDUA5qLEpP8e+n+65xsp5rLQhhCWfBJ+78w==";
        
        // ** 인코딩 : UjeRRoTT0jV849PNvUg%2F0H%2BxNBxqMtejj1%2F9WiC93CmJrbGGBkyBDUA5qLEpP8e%2Bn%2B65xsp5rLQhhCWfBJ%2B78w%3D%3D
        // String serviceKey = "UjeRRoTT0jV849PNvUg%2F0H%2BxNBxqMtejj1%2F9WiC93CmJrbGGBkyBDUA5qLEpP8e%2Bn%2B65xsp5rLQhhCWfBJ%2B78w%3D%3D";
    	
        // 직접 인코딩된 serviceKey 사용
        return apiUrl +
                "?serviceKey=" + serviceKey +
                "&numOfRows=10" +
                "&pageNo=1" +
                "&base_date=" + baseDate +
                "&base_time=" + baseTime +
                "&nx=" + nx +
                "&ny=" + ny +
                "&dataType=JSON";
        
//    	return UriComponentsBuilder.fromHttpUrl(apiUrl)
//    		        .queryParam("serviceKey", serviceKey)  // 이미 인코딩된 키는 추가 인코딩 방지
//    		        .queryParam("numOfRows", 10)
//    		        .queryParam("pageNo", 1)
//    		        .queryParam("base_date", baseDate)
//    		        .queryParam("base_time", baseTime)
//    		        .queryParam("nx", nx)
//    		        .queryParam("ny", ny)
//    		        .queryParam("dataType", "JSON")
//    		        .toUriString();
    }

    private void saveApiResponse(List<ApiItem> items, NxNyInfo nxNy) {
    	for (ApiResponse.ApiItem item : items) {
            NxNyGetUltraSrtFcst forecast = new NxNyGetUltraSrtFcst();
            forecast.setNx(nxNy.getNx());
            forecast.setNy(nxNy.getNy());
            forecast.setBaseDate(item.getBaseDate());
            forecast.setBaseTime(item.getBaseTime());
            forecast.setCategory(item.getCategory());
            forecast.setFcstValue(item.getObsrValue());
            forecast.setFcstUnit(getUnitForCategory(item.getCategory()));
            repository.save(forecast);
        }
    }

    private void deleteOldData(String baseDate, String baseTime) {
        String previousBaseDate = calculatePreviousBaseDate(baseDate, baseTime);
        String previousBaseTime = calculatePreviousBaseTime(baseTime);
        repository.deleteByBaseDateAndBaseTime(previousBaseDate, previousBaseTime);
    }

    // 현재 날짜와 시간 처리 함수 (생략)
    // 예: calculatePreviousBaseDate(), calculatePreviousBaseTime(), getCurrentBaseDate(), getCurrentBaseTime()
    public String getCurrentBaseDate() {
        // 현재 날짜를 YYYYMMDD 형식으로 반환
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(formatter);
    }
    
    public String getCurrentBaseTime() {
        // 현재 시간을 HHMM 형식으로 반환 (정시에 해당하는 시간)
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        return String.format("%02d00", hour);  // 2자리 시간 + 00분으로 반환
    }
    
    public String calculatePreviousBaseDate(String baseDate, String baseTime) {
        LocalDateTime dateTime = LocalDateTime.parse(baseDate + baseTime, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        
        // 이전 시간으로 이동 (1시간 전)
        LocalDateTime previousDateTime = dateTime.minusHours(1);
        
        // 새로운 baseDate를 반환 (YYYYMMDD)
        return previousDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String calculatePreviousBaseTime(String baseTime) {
        // baseDate와 동일하게 1시간 전으로 계산
        int hour = Integer.parseInt(baseTime.substring(0, 2));
        int previousHour = (hour == 0) ? 23 : hour - 1;  // 00시이면 23시로 변경
        return String.format("%02d00", previousHour);
    }
    
    private String getUnitForCategory(String category) {
        switch (category) {
            case "T1H":  // 기온
                return "℃";
            case "RN1":  // 1시간 강수량
                return "mm";
            case "UUU":  // 동서 바람 성분
                return "m/s";
            case "VVV":  // 남북 바람 성분
                return "m/s";
            case "REH":  // 습도
                return "%";
            case "PTY":  // 강수 형태 (코드 값)
                return "코드값";
            case "VEC":  // 풍향
                return "deg";
            case "WSD":  // 풍속
                return "m/s";
            default:
                return "";  // 알 수 없는 category일 경우 빈 문자열 반환
        }
    }

}


