package com.fulaan.train.dto;

import com.pojo.train.CriticismEntry;

import java.util.List;

/**
 * Created by admin on 2016/12/6.
 */
public class CriticismDTO {
    private String id;
    private String instituteId;
    private String userId;
    private String comment;
    private int score;
    private String avatar;
    private String nickName;
    private List<Integer> scoreList;
    private List<Integer> unScoreList;

    public CriticismDTO(){

    }

    public CriticismDTO(CriticismEntry entry){
        this.id=entry.getID().toString();
        this.instituteId=entry.getInstituteId().toString();
        this.userId=entry.getUserId().toString();
        this.comment=entry.getComment();
        this.score=entry.getScore();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public List<Integer> getUnScoreList() {
        return unScoreList;
    }

    public void setUnScoreList(List<Integer> unScoreList) {
        this.unScoreList = unScoreList;
    }
}
