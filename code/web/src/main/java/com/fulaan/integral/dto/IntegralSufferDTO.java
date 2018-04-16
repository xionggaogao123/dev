package com.fulaan.integral.dto;

/**
 * Created by James on 2018-04-16.
 */
public class IntegralSufferDTO {
    /* * id                                  id
 * userId          uid                用户id
 * score           sco                积分
 * suffer          suf                经验值*/

    private String id;
    private String userId;
    private int score;
    private int suffer;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSuffer() {
        return suffer;
    }

    public void setSuffer(int suffer) {
        this.suffer = suffer;
    }
}
