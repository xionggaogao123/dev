package com.fulaan.examresult.controller;

/**
 * Created by fl on 2015/6/24.
 */
public class ClassScoreDTO {

    private String className;
    private Double score;


    public ClassScoreDTO(String className, Double score) {
        this.className = className;
        this.score = score;
    }

    public ClassScoreDTO() {}

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
