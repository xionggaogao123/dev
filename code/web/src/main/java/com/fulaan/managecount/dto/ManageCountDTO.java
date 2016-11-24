package com.fulaan.managecount.dto;

/**
 * Created by guojing on 2015/4/13.
 */
public class ManageCountDTO {
    private String id;

    private String userId;

    private String name;

    private int newCountTotal;

    private int countTotal;

    private String createTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNewCountTotal() {
        return newCountTotal;
    }

    public void setNewCountTotal(int newCountTotal) {
        this.newCountTotal = newCountTotal;
    }

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
