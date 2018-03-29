package com.fulaan.reportCard.dto;

/**
 * Created by James on 2018-03-29.
 */
public class VirtualAndUserDTO {
    private String id;
    private String communityId;
    private String virtualId;
    private String userId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getVirtualId() {
        return virtualId;
    }

    public void setVirtualId(String virtualId) {
        this.virtualId = virtualId;
    }
}
