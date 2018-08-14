package com.fulaan.communityValidate.dto;

import com.pojo.fcommunity.ValidateGroupInfoEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by James on 2018-08-07.
 */
public class ValidateGroupInfoDTO {

    private String id;
    private String userId;
    private String reviewedId;
    private String contactId;
    private int status;
    private int contactType;
    private int approvedStatus;
    private String backTime;
    private String createTime;
    private String applyMessage;

    public ValidateGroupInfoDTO(){

    }
    public ValidateGroupInfoDTO(ValidateGroupInfoEntry entry){
        if(entry!=null){
            this.id = entry.getID()==null?"":entry.getID().toString();
            this.userId = entry.getUserId()==null?"":entry.getUserId().toString();
            this.reviewedId = entry.getReviewedId()==null?"":entry.getReviewedId().toString();
            this.contactId = entry.getContactId()==null?"":entry.getContactId().toString();
            this.status = entry.getStatus();
            this.contactType  = entry.getContactType();
            this.approvedStatus = entry.getApprovedStatus();
            if(entry.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(entry.getCreateTime());
            }else{
                this.createTime = "";
            }
            if(entry.getBackTime()!=0l){
                this.backTime = DateTimeUtils.getLongToStrTimeTwo(entry.getBackTime());
            }else{
                this.backTime = "";
            }
            this.applyMessage = entry.getApplyMessage();
        }else{
            new ValidateGroupInfoDTO();
        }

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

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public int getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(int approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getApplyMessage() {
        return applyMessage;
    }

    public void setApplyMessage(String applyMessage) {
        this.applyMessage = applyMessage;
    }
}
