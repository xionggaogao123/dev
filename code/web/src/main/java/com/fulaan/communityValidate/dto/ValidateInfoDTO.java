package com.fulaan.communityValidate.dto;

import com.pojo.fcommunity.ValidateInfoEntry;
import com.sys.utils.DateTimeUtils;

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
    private String time;
    private String roleStr;



    private int owner;
    private String communityName;

    private String userName;
    private String reviewName;


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
        this.roleStr=entry.getRoleStr();
        this.time= DateTimeUtils.convert(entry.getID().getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
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

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public int getReviewState() {
        return reviewState;
    }

    public void setReviewState(int reviewState) {
        this.reviewState = reviewState;
    }

    public String getReviewKeyId() {
        return reviewKeyId;
    }

    public void setReviewKeyId(String reviewKeyId) {
        this.reviewKeyId = reviewKeyId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }
}
