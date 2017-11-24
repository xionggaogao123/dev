package com.fulaan.reportCard.dto;

/**
 * Created by scott on 2017/11/24.
 */
public class VirtualCommunityUserDTO {

    private String communityId;
    private String communityName;
    private int userCount;

    public VirtualCommunityUserDTO(){

    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
