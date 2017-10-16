package com.fulaan.reportCard.dto;

/**
 * Created by scott on 2017/10/16.
 */
public class ExamGroupUserScoreDTO {

    private String id;
    private double score;
    private int scoreLevel;
    private int rank;

    public GroupExamUserRecordDTO buildDTO(){
        GroupExamUserRecordDTO userRecordDTO=new GroupExamUserRecordDTO();
        userRecordDTO.setId(this.id);
        userRecordDTO.setScore(this.score);
        userRecordDTO.setScoreLevel(this.scoreLevel);
        userRecordDTO.setRank(this.rank);
        return userRecordDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(int scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
