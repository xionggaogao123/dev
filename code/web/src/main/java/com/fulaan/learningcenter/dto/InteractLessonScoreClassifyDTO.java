package com.fulaan.learningcenter.dto;

import com.pojo.school.InteractLessonScoreClassifyEntry;

/**
 * Created by guojing on 2015/12/2.
 */
public class InteractLessonScoreClassifyDTO {

    private String id;
    private String lessonId;
    private int times;
    private int excellentNum;
    private int excellentRate;
    private int goodNum;
    private int goodRate;
    private int failureNum;
    private int failureRate;

    public InteractLessonScoreClassifyDTO(){

    }

    public InteractLessonScoreClassifyDTO(InteractLessonScoreClassifyEntry entry) {
        if(entry !=null) {
            this.id=entry.getID().toString();

            this.lessonId=entry.getLessonId().toString();

            this.times=entry.getTimes();

            this.excellentNum=entry.getExcellentNum();

            this.excellentRate=entry.getExcellentRate();

            this.goodNum=entry.getGoodNum();

            this.goodRate=entry.getGoodRate();

            this.failureNum=entry.getFailureNum();

            this.failureRate=entry.getFailureRate();
        }else{
            new InteractLessonScoreClassifyDTO();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getExcellentNum() {
        return excellentNum;
    }

    public void setExcellentNum(int excellentNum) {
        this.excellentNum = excellentNum;
    }

    public int getExcellentRate() {
        return excellentRate;
    }

    public String getExcellentRateStr() {
        return getExcellentRate()+"%";
    }

    public void setExcellentRate(int excellentRate) {
        this.excellentRate = excellentRate;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getGoodRate() {
        return goodRate;
    }

    public String getGoodRateStr() {
        return getGoodRate()+"%";
    }

    public void setGoodRate(int goodRate) {
        this.goodRate = goodRate;
    }

    public int getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(int failureNum) {
        this.failureNum = failureNum;
    }

    public int getFailureRate() {
        return failureRate;
    }

    public String getFailureRateStr() {
        return getFailureRate()+"%";
    }

    public void setFailureRate(int failureRate) {
        this.failureRate = failureRate;
    }
}
