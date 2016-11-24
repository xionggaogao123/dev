package com.fulaan.examresult.controller;

import java.util.Comparator;

/**用于学生历史成绩
 * Created by fl on 2015/7/1.
 */
public class SubjectExamDTO {
    private String examName;
    private String date;
    private Double score;
    private Double classAverageScore;
    private Integer classRanking;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getClassAverageScore() {
        return Math.round(classAverageScore*10)/10.0;
    }

    public void setClassAverageScore(Double classAverageScore) {
        this.classAverageScore = classAverageScore;
    }

    public Integer getClassRanking() {
        return classRanking;
    }

    public void setClassRanking(Integer classRanking) {
        this.classRanking = classRanking;
    }
}

class SortByDate1 implements Comparator {
    public int compare(Object o1, Object o2) {
        SubjectExamDTO e1 = (SubjectExamDTO) o1;
        SubjectExamDTO e2 = (SubjectExamDTO) o2;
        return e1.getDate().compareTo(e2.getDate());
    }
}

class SortByScore implements Comparator {
    public int compare(Object o1, Object o2) {
        SubjectExamDTO e1 = (SubjectExamDTO) o1;
        SubjectExamDTO e2 = (SubjectExamDTO) o2;
        Double num1 = e1.getScore()==null ? 0 : e1.getScore();
        Double num2 = e2.getScore()==null ? 0 : e2.getScore();
        return num2.compareTo(num1);
    }
}

class SortByClassScore implements Comparator {
    public int compare(Object o1, Object o2) {
        SubjectExamDTO e1 = (SubjectExamDTO) o1;
        SubjectExamDTO e2 = (SubjectExamDTO) o2;
        return e2.getClassAverageScore().compareTo(e1.getClassAverageScore());
    }
}

class SortByClassRanking implements Comparator {
    public int compare(Object o1, Object o2) {
        SubjectExamDTO e1 = (SubjectExamDTO) o1;
        SubjectExamDTO e2 = (SubjectExamDTO) o2;
        return e1.getClassRanking().compareTo(e2.getClassRanking());
    }
}
