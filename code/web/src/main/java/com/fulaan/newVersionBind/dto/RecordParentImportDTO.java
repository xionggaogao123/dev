package com.fulaan.newVersionBind.dto;

import com.pojo.user.RecordParentImportEntry;

/**
 * Created by scott on 2018/1/12.
 */
public class RecordParentImportDTO {
    private String id;
    private String parentId;
    private String userKey;
    private String nickName;

    public RecordParentImportDTO(){

    }

    public RecordParentImportDTO(RecordParentImportEntry entry){
        this.id=entry.getID().toString();
        this.parentId=entry.getParentId().toString();
        this.userKey=entry.getUserKey();
        this.nickName=entry.getNickName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
