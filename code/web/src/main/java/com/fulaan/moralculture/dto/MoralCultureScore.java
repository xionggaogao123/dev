package com.fulaan.moralculture.dto;

import java.util.Date;

/**
 * Created by guojing on 2015/7/2.
 */
public class MoralCultureScore {

    /**
     * id
     */
    private String id;

    /**
     * 德育id
     */
    private String projectId;

    /**
     * 德育分数
     */
    private String projectScore;

    /**
     * 新建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectScore() {
        return projectScore;
    }

    public void setProjectScore(String projectScore) {
        this.projectScore = projectScore;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
