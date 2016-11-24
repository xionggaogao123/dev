package com.fulaan.learningcenter.dto;

import com.pojo.school.InteractLessonQuickAnswerEntry;

/**
 * Created by guojing on 2015/12/1.
 */
public class InteractLessonQuickAnswerDTO {
    private String id;

    private String lessonId;

    private String userId;

    private String userName;

    private String format;

    private String formatDes;

    private int answer;

    private String answerDes;

    private String useTime;

    public InteractLessonQuickAnswerDTO(){

    }

    public InteractLessonQuickAnswerDTO(InteractLessonQuickAnswerEntry entry) {
        if(entry !=null) {
            this.id=entry.getID().toString();

            this.lessonId=entry.getLessonId().toString();

            this.userId=entry.getUserId().toString();

            this.format=entry.getFormat();

            this.formatDes=entry.getFormatDes();

            this.answer=entry.getAnswer();

            this.answerDes=entry.getAnswerDes();

            this.useTime=entry.getUseTime();
        }else{
            new InteractLessonQuickAnswerDTO();
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormatDes() {
        return formatDes;
    }

    public void setFormatDes(String formatDes) {
        this.formatDes = formatDes;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getAnswerDes() {
        return answerDes;
    }

    public void setAnswerDes(String answerDes) {
        this.answerDes = answerDes;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
}
