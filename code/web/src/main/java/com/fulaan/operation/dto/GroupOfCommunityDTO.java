package com.fulaan.operation.dto;

/**
 * Created by scott on 2017/10/13.
 */
public class GroupOfCommunityDTO {

    private String groupId;
    private String communityId;
    private String groupName;

    public GroupOfCommunityDTO(){

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
