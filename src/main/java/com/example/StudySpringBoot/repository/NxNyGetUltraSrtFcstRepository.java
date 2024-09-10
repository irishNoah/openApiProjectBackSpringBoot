package com.example.StudySpringBoot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StudySpringBoot.entity.NxNyGetUltraSrtFcst;

public interface NxNyGetUltraSrtFcstRepository  extends JpaRepository<NxNyGetUltraSrtFcst, Long>{
	// 특정 baseDate와 baseTime으로 데이터를 삭제하는 메소드
    void deleteByBaseDateAndBaseTime(String baseDate, String baseTime);

    // nx, ny 정보를 기반으로 데이터 검색
    List<NxNyGetUltraSrtFcst> findByNxAndNy(String nx, String ny);
}
