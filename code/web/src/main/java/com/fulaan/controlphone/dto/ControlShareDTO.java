package com.fulaan.controlphone.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-17.
 */
public class ControlShareDTO {

    private String id;

    private String userId;

    private String shareId;

    private String sonId;

    private int type;

    private List<String> roleType = new ArrayList<String>();

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

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getSonId() {
        return sonId;
    }

    public void setSonId(String sonId) {
        this.sonId = sonId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getRoleType() {
        return roleType;
    }

    public void setRoleType(List<String> roleType) {
        this.roleType = roleType;
    }
}
