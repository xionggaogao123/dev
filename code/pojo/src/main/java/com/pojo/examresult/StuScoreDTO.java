package com.pojo.examresult;

/**
 * Created by fl on 2015/6/19.
 */
public class StuScoreDTO implements Comparable<StuScoreDTO>{
    String studentName;
    Double score;
    Integer classRanking;
    Integer gradeRanking;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getClassRanking() {
        return classRanking;
    }

    public void setClassRanking(Integer classRanking) {
        this.classRanking = classRanking;
    }

    public Integer getGradeRanking() {
        return gradeRanking;
    }

    public void setGradeRanking(Integer gradeRanking) {
        this.gradeRanking = gradeRanking;
    }

    @Override
    public int compareTo(StuScoreDTO arg0) {
        return this.getClassRanking().compareTo(arg0.getClassRanking());
    }


}
