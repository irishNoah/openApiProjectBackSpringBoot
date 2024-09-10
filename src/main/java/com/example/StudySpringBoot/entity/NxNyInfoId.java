package com.example.StudySpringBoot.entity;

import java.io.Serializable;
import java.util.Objects;

public class NxNyInfoId implements Serializable {
    private String nx;
    private String ny;

    // Default constructor
    public NxNyInfoId() {}

    public NxNyInfoId(String nx, String ny) {
        this.nx = nx;
        this.ny = ny;
    }

    // Getters and setters
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

    // equals() and hashCode() must be overridden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NxNyInfoId that = (NxNyInfoId) o;
        return Objects.equals(nx, that.nx) && Objects.equals(ny, that.ny);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nx, ny);
    }
}