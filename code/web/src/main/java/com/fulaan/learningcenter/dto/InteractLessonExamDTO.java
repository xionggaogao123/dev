package com.fulaan.learningcenter.dto;

import com.pojo.school.InteractLessonExamEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/12/3.
 */
public class InteractLessonExamDTO {
    private String id;

    private String lessonId;

    private String userId;

    private String userName;

    private int times;

    private String examName;

    private int correctRate;

    private String useTime;

    private List<InteractLessonExamDetailDTO> examDetailList=new ArrayList<InteractLessonExamDetailDTO>();

    public InteractLessonExamDTO(){

    }

    public InteractLessonExamDTO(InteractLessonExamEntry entry) {
        if(entry !=null) {
            this.id=entry.getID().toString();

            this.lessonId=entry.getLessonId().toString();

            this.userId=entry.getUserId().toString();

            this.times=entry.getTimes();

            this.examName=entry.getExamName();

            this.correctRate=entry.getCorrectRate();

            this.useTime=entry.getUseTime();
        }else{
            new InteractLessonExamDTO();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getCorrectRate() {
        return correctRate;
    }

    public String getCorrectRateStr() {
        return getCorrectRate()+"%";
    }

    public void setCorrectRate(int correctRate) {
        this.correctRate = correctRate;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public List<InteractLessonExamDetailDTO> getExamDetailList() {
        return examDetailList;
    }

    public void setExamDetailList(List<InteractLessonExamDetailDTO> examDetailList) {
        this.examDetailList = examDetailList;
    }
}
