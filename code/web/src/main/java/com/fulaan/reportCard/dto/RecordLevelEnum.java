package com.fulaan.reportCard.dto;

/**
 * Created by scott on 2017/9/30.
 * A+:100 A:99 A-:98 B+:97 B:96 B-:95 C+:94 C:93 C-:92 D+:91 D:90 D-:89
 */
public enum RecordLevelEnum {
    AP(98),BP(95),CP(92),DP(89);
    private int levelScore;

    private RecordLevelEnum(int levelScore){
        this.levelScore=levelScore;
    }

    public int getLevelScore() {
        return levelScore;
    }

    public void setLevelScore(int levelScore) {
        this.levelScore = levelScore;
    }
}
