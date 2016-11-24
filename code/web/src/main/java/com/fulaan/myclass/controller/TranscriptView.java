package com.fulaan.myclass.controller;

/**
 * Created by yan on 14-10-17.
 */
public class TranscriptView {
    private String classId;
    private String userId;
    private String classname;
    private String teacherNAME;

    private String schoolName;

    private int termType;

    private String subjectName;

    private String nickName;
    private String studentNum;

    private String teacherComments;
    private String resultsPicSrc;
    private Integer finalResult;
    private Integer usualResult;
    private String termName;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getTeacherNAME() {
        return teacherNAME;
    }

    public void setTeacherNAME(String teacherNAME) {
        this.teacherNAME = teacherNAME;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getTeacherComments() {
        return teacherComments;
    }

    public void setTeacherComments(String teacherComments) {
        this.teacherComments = teacherComments;
    }

    public String getResultsPicSrc() {
        return resultsPicSrc;
    }

    public void setResultsPicSrc(String resultsPicSrc) {
        this.resultsPicSrc = resultsPicSrc;
    }

    public Integer getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(Integer finalResult) {
        this.finalResult = finalResult;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getUsualResult() {
        return usualResult;
    }

    public void setUsualResult(Integer usualResult) {
        this.usualResult = usualResult;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }
}
