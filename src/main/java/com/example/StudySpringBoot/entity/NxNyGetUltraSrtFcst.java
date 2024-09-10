package com.example.StudySpringBoot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nx_ny_getUltraSrtFcst")
public class NxNyGetUltraSrtFcst {
	@Id
    @Column(name = "id")  // 예시로 id 필드를 사용 (실제 primary key에 맞게 설정)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 식별자 필드 추가
	
	@Column(name = "nx", length = 50, nullable = false)
    private String nx;

    @Column(name = "ny", length = 50, nullable = false)
    private String ny;

    @Column(name = "baseDate", length = 8, nullable = false)
    private String baseDate;

    @Column(name = "baseTime", length = 4, nullable = false)
    private String baseTime;

    @Column(name = "category", length = 50, nullable = false)
    private String category;

    @Column(name = "fcstValue", length = 10, nullable = false)
    private String fcstValue;

    @Column(name = "fcstUnit", length = 20, nullable = false)
    private String fcstUnit;

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

	public String getFcstValue() {
		return fcstValue;
	}

	public void setFcstValue(String fcstValue) {
		this.fcstValue = fcstValue;
	}

	public String getFcstUnit() {
		return fcstUnit;
	}

	public void setFcstUnit(String fcstUnit) {
		this.fcstUnit = fcstUnit;
	}
    
}
