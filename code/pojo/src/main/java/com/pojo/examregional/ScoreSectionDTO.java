package com.pojo.examregional;

/**
 * Created by fl on 2015/11/3.
 */
public class ScoreSectionDTO {
    private int beginScore;
    private int endScore;
    private int num;

    public ScoreSectionDTO(ScoreSection scoreSection){
        this.beginScore = scoreSection.getBeginScore();
        this.endScore = scoreSection.getEndScore();
        this.num = scoreSection.getNum();
    }

    public ScoreSectionDTO(int beginScore, int endScore, int num) {
        this.beginScore = beginScore;
        this.endScore = endScore;
        this.num = num;
    }

    public int getBeginScore() {
        return beginScore;
    }

    public void setBeginScore(int beginScore) {
        this.beginScore = beginScore;
    }

    public int getEndScore() {
        return endScore;
    }

    public void setEndScore(int endScore) {
        this.endScore = endScore;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
