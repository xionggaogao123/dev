package com.fulaan.quality.dto;

/**
 * Created by wang_xinxin on 2016/10/24.
 */
public class TeacherQualityDTO {

    private String teacherId;

    private String teacherName;

    private String gradeName;

    private int lessonCnt;

    private int wareCnt;

    private int examCnt;

    private int sysScore;

    private String comment;

    private String score;

    private int type;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getLessonCnt() {
        return lessonCnt;
    }

    public void setLessonCnt(int lessonCnt) {
        this.lessonCnt = lessonCnt;
    }

    public int getWareCnt() {
        return wareCnt;
    }

    public void setWareCnt(int wareCnt) {
        this.wareCnt = wareCnt;
    }

    public int getExamCnt() {
        return examCnt;
    }

    public void setExamCnt(int examCnt) {
        this.examCnt = examCnt;
    }

    public int getSysScore() {
        return sysScore;
    }

    public void setSysScore(int sysScore) {
        this.sysScore = sysScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
