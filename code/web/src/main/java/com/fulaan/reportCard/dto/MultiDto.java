package com.fulaan.reportCard.dto;

/**
 * 
 * <简述>个人成绩
 * <详细描述>
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
public class MultiDto {

    private String score;
    
    private String scoreLevel;
    
    private String bc;
    
    private String xc;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(String scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public String getBc() {
        return bc;
    }

    public void setBc(String bc) {
        this.bc = bc;
    }

    public String getXc() {
        return xc;
    }

    public void setXc(String xc) {
        this.xc = xc;
    }
    
    
}
