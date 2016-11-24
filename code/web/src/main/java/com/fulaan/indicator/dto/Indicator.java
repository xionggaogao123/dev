package com.fulaan.indicator.dto;

import com.pojo.indicator.InterestEvaluate;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2016/11/7.
 */
public class Indicator {
    private String zhiBiaoId;
    private int type;
    private String zhiBiaoName;
    private String zhiBiaoParentId;
    private int level;
    private int scoreType;
    private String score;

    public Indicator() {

    }

    public Indicator(InterestEvaluate e) {
        this.zhiBiaoId = e.getZhiBiaoId() == null ? "" : e.getZhiBiaoId().toString();
        this.scoreType = e.getScoreType();
        this.score = e.getScore() == null ? "" : e.getScore();
    }

    public String getZhiBiaoId() {
        return zhiBiaoId;
    }

    public void setZhiBiaoId(String zhiBiaoId) {
        this.zhiBiaoId = zhiBiaoId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getZhiBiaoName() {
        return zhiBiaoName;
    }

    public void setZhiBiaoName(String zhiBiaoName) {
        this.zhiBiaoName = zhiBiaoName;
    }

    public String getZhiBiaoParentId() {
        return zhiBiaoParentId;
    }

    public void setZhiBiaoParentId(String zhiBiaoParentId) {
        this.zhiBiaoParentId = zhiBiaoParentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScoreType() {
        return scoreType;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = scoreType;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public InterestEvaluate buildInterestEvaluate() {
        InterestEvaluate entry=new InterestEvaluate(
                new ObjectId(this.getZhiBiaoId()),
                this.getScoreType(),
                this.getScore()
        );
        return entry;
    }
}
