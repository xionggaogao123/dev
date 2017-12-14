package com.fulaan.newVersionBind.dto;

import com.pojo.user.MakeOutUserRelationEntry;

/**
 * Created by scott on 2017/12/14.
 */
public class MakeOutUserRealationDTO {

    private String id;
    private String userId;
    private String userKey;

    public MakeOutUserRealationDTO(){

    }

    public MakeOutUserRealationDTO(MakeOutUserRelationEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getParentId().toString();
        this.userKey=entry.getUserKey();
    }

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

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
