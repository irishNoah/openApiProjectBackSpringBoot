package com.example.StudySpringBoot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StudySpringBoot.entity.NxNyInfo;
import com.example.StudySpringBoot.entity.NxNyInfoId;

public interface NxNyInfoRepository extends JpaRepository<NxNyInfo, NxNyInfoId>{
	// nx, ny 정보를 오름차순으로 가져오는 메소드
    List<NxNyInfo> findAllByOrderByNxAscNyAsc();
}
