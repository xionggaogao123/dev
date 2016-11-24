package com.fulaan.duty.dto;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/6.
 */
public class DutyDTO {
    private String id;

    private String dutyProject;

    private String usernames;

    private int xIndex;

    private int yIndex;

    private int index;

    private String[] users;

    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDutyProject() {
        return dutyProject;
    }

    public void setDutyProject(String dutyProject) {
        this.dutyProject = dutyProject;
    }

    public String getUsernames() {
        return usernames;
    }

    public void setUsernames(String usernames) {
        this.usernames = usernames;
    }

    public int getxIndex() {
        return xIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
