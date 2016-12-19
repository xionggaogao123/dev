package com.fulaan.community.dto;

import com.pojo.fcommunity.CommunitySystemInfoEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by admin on 2016/11/29.
 */
public class CommunitySystemInfoDTO {

    private String id;
    private String roleStr;
    private String userId;
    private int type;
    private String communityId;
    private String time;
    private String communityName;
    private String nickName;

    public CommunitySystemInfoDTO(CommunitySystemInfoEntry entry){
        this.id=entry.getID().toString();
        this.roleStr=entry.getRoleStr();
        this.userId=entry.getUserId().toString();
        this.type=entry.getType();
        this.communityId=entry.getCommunityId().toString();
        this.time= DateTimeUtils.convert(entry.getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
