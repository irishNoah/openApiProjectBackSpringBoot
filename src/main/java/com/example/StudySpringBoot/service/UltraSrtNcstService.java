package com.example.StudySpringBoot.service;

import java.net.URI;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

// 외부 API를 연동할 때 고려하면 좋은 점들
// https://dkswnkk.tistory.com/742#7.-%EC%BA%90%EC%8B%B1-%EC%B2%98%EB%A6%AC%EB%A5%BC-%EA%B3%A0%EB%A0%A4%ED%95%B4%EB%9D%BC 
@Service
public class UltraSrtNcstService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // nx, ny 정보를 DB에서 가져오는 메서드
    private List<NxNyInfo> getNxNyInfoList() {
        String sql = "SELECT nx, ny FROM nx_ny_info ORDER BY nx ASC, ny ASC";

        // 데이터베이스에서 nx, ny 값들을 가져와 리스트로 반환
        return jdbcTemplate.query(sql, (rs, rowNum) -> new NxNyInfo(rs.getInt("nx"), rs.getInt("ny")));
    }

    // API 호출 및 데이터 저장 서비스
    public void fetchAndSaveUltraSrtNcstData() {
        List<NxNyInfo> nxNyInfoList = getNxNyInfoList();  // DB에서 nx, ny 가져오기

        for (NxNyInfo info : nxNyInfoList) {
            String baseDate = getCurrentDate();
            String baseTime = getCurrentTimeUltraNcst();

            // API 요청 URL 생성
            String url = createUrl(info.getNx(), info.getNy(), baseDate, baseTime);

            try {
                // API 요청 및 JSON 응답 수신
                String response = restTemplate.getForObject(URI.create(url), String.class);

                // 응답 파싱 및 DB 삽입
                parseAndInsertResponse(response);

            } catch (HttpClientErrorException e) {
                // 4xx 오류 처리
                System.out.println("4xx 오류 발생: nx=" + info.getNx() + ", ny=" + info.getNy() + ". 오류: " + e.getMessage());
            } catch (HttpServerErrorException e) {
                // 5xx 오류 처리
                System.out.println("5xx 오류 발생: nx=" + info.getNx() + ", ny=" + info.getNy() + ". 오류: " + e.getMessage());
            } catch (Exception e) {
                // 그 외의 모든 오류 처리
                System.out.println("기타 오류 발생: nx=" + info.getNx() + ", ny=" + info.getNy() + ". 오류: " + e.getMessage());
            }
            
            // 오류가 발생해도 다음 nx, ny 값을 처리하기 위해 루프는 계속됨
        }
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
        
        System.out.println("result url >>>>> " + resultUrl);
        
        return resultUrl;
    }

    // JSON 응답 파싱 및 데이터베이스 삽입
    private void parseAndInsertResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            JsonNode items = root.path("response").path("body").path("items").path("item");

            // 각 아이템 데이터를 반복하면서 데이터베이스에 삽입
            for (JsonNode item : items) {
                String baseDate = item.path("baseDate").asText();
                String baseTime = item.path("baseTime").asText();
                String category = item.path("category").asText();
                String obsrValue = item.path("obsrValue").asText();
                String nx = item.path("nx").asText();
                String ny = item.path("ny").asText();

                // SQL 삽입 쿼리 실행
                String sql = "INSERT INTO nx_ny_getUltraSrtNcst (nx, ny, baseDate, baseTime, category, obsrValue, unit) VALUES (?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(sql, nx, ny, baseDate, baseTime, category, obsrValue, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    
    // NxNyInfo 클래스 (내부 클래스)
    public static class NxNyInfo {
        private int nx;
        private int ny;

        public NxNyInfo(int nx, int ny) {
            this.nx = nx;
            this.ny = ny;
        }

        public int getNx() {
            return nx;
        }

        public int getNy() {
            return ny;
        }
    }
}
