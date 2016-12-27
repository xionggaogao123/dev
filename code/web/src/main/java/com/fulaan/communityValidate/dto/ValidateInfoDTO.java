package com.fulaan.communityValidate.dto;

import com.pojo.fcommunity.ValidateInfoEntry;

/**
 * Created by admin on 2016/12/26.
 */
public class ValidateInfoDTO {
    private String id;
    private String userId;
    private String reviewedId;
    private String applyMessage;
    private String approvedId;
    private int approvedStatus;
    private int status;
    private int type;
    private String communityId;
    private int authority;
    private int way;
    private int reviewState;
    private String reviewKeyId;



    private int owner;
    private String communityName;


    public ValidateInfoDTO(ValidateInfoEntry entry){
        this.id=entry.getID().toString();
        if(null!=entry.getUserId()) {
            this.userId = entry.getUserId().toString();
        }
        this.reviewedId=entry.getReviewedId().toString();
        this.applyMessage=entry.getApplyMessage();
        if(null!=entry.getApprovedId()){
            this.approvedId=entry.getApprovedId().toString();
        }
        this.approvedStatus=entry.getApprovedStatus();
        this.status=entry.getStatus();
        this.type=entry.getType();
        this.communityId=entry.getCommunityId().toString();
        this.authority=entry.getAuthority();
        this.way=entry.getWay();
        this.reviewState=entry.getReviewState();
        this.reviewKeyId=entry.getReviewKeyId().toString();
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

    public String getReviewedId() {
        return reviewedId;
    }

    public void setReviewedId(String reviewedId) {
        this.reviewedId = reviewedId;
    }

    public String getApplyMessage() {
        return applyMessage;
    }

    public void setApplyMessage(String applyMessage) {
        this.applyMessage = applyMessage;
    }

    public String getApprovedId() {
        return approvedId;
    }

    public void setApprovedId(String approvedId) {
        this.approvedId = approvedId;
    }

    public int getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(int approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
