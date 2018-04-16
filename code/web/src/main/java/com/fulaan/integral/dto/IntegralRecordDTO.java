package com.fulaan.integral.dto;

/**
 * Created by James on 2018-04-16.
 */
public class IntegralRecordDTO {

    /* * id                        id
 * userId                    uid                    用户id
 * score                     sco                    分值
 * module                    mod                    模块
 * dateTime                  dtm                    日期
 * createTime                ctm                    创建时间
 * sort                      sor                    顺序*/
    private String id;
    private String userId;
    private int score;
    private String module;
    private String dateTime;
    private String createTime;
    private int sort;

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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
