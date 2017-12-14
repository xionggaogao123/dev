package com.fulaan.newVersionBind.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/14.
 */
public class TransferUserRelationDTO {

    private String communityId;
    private List<String> userIds = new ArrayList<String>();

    public TransferUserRelationDTO(){

    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
