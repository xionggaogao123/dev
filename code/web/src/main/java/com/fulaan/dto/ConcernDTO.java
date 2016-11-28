package com.fulaan.dto;

import com.pojo.fcommunity.ConcernEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/7.
 */
public class ConcernDTO {

    private String id;
    private String userId;
    private String concernId;
    private String avatar;
    private String nickName;

    public ConcernDTO(ConcernEntry concernEntry) {
        this.id = concernEntry.getID().toString();
        this.userId = concernEntry.getUserId().toString();
        this.concernId = concernEntry.getConcernId().toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConcernId() {
        return concernId;
    }

    public void setConcernId(String concernId) {
        this.concernId = concernId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
