package com.fulaan.moralculture.dto;

import java.util.List;

/**
 * Created by guojing on 2015/7/2.
 */
public class MoralCultureDTO {

    /**
     * id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 学校id
     */
    private String schoolId;

    /**
     * 年级id
     */
    private String gradeId;

    /**
     * 班级id
     */
    private String classId;

    /**
     *学期
     */
    private String semesterId;

    /**
     * 提交状态
     */
    private int state;

    /**
     * 提交状态描述
     */
    private String stateDesc;

    /**
     * 德育分数集合
     */
    private List<MoralCultureScore> moralCultureScores;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public List<MoralCultureScore> getMoralCultureScores() {
        return moralCultureScores;
    }

    public void setMoralCultureScores(List<MoralCultureScore> moralCultureScores) {
        this.moralCultureScores = moralCultureScores;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }
}
