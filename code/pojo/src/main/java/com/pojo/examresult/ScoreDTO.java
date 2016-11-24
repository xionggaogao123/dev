package com.pojo.examresult;

import java.util.Comparator;

/**
 * Created by fl on 2015/6/19.
 */
public class ScoreDTO {
    String subjectName;  //科目名称
    String className;  //班级名称
    String studentName;  //学生姓名
    Double usualScore;  //平时成绩
    Double finalScore;  //期末成绩
    Double midtermScore;  //期中成绩

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(Double usualScore) {
        this.usualScore = usualScore;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    public Double getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(Double midtermScore) {
        this.midtermScore = midtermScore;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}






