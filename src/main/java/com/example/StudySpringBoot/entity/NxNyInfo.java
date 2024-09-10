package com.example.StudySpringBoot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nx_ny_info")  // 실제 DB 테이블명과 매핑
@IdClass(NxNyInfoId.class)  // 복합 키 클래스 설정
public class NxNyInfo {
		// private String id;  // 예시로 사용, 실제 primary key에 맞게 설정

		@Id
	    @Column(name = "nx", length = 50, nullable = false)
	    private String nx;

		@Id
	    @Column(name = "ny", length = 50, nullable = false)
	    private String ny;

	    public String getNx() {
	        return nx;
	    }

	    public void setNx(String nx) {
	        this.nx = nx;
	    }

	    public String getNy() {
	        return ny;
	    }

	    public void setNy(String ny) {
	        this.ny = ny;
	    }
}
