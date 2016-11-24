package com.fulaan.microlesson.dto;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/4/20.
 */
public class ScoreDTO {
    private String name;

    private String score;

    private List<Integer> scorelist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getScorelist() {
        return scorelist;
    }

    public void setScorelist(List<Integer> scorelist) {
        this.scorelist = scorelist;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
