package com.pojo.teacherevaluation;

/**
 * Created by fl on 2016/4/28.
 */
public class RankingDTO implements Comparable<RankingDTO>{

    private String teacherId;
    private String teacherName;
    private String groupName;
    private double finalStdScore;
    private int ranking;
    private String gradeName;

    public RankingDTO(){}

    public RankingDTO(String teacherId, String teacherName, String groupName, double finalStdScore, int ranking) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.groupName = groupName;
        this.finalStdScore = finalStdScore;
        this.ranking = ranking;
    }

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getFinalStdScore() {
        return finalStdScore;
    }

    public void setFinalStdScore(double finalStdScore) {
        this.finalStdScore = finalStdScore;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    @Override
    public int compareTo(RankingDTO o) {
        return o.getFinalStdScore().compareTo(this.getFinalStdScore());
    }
}
