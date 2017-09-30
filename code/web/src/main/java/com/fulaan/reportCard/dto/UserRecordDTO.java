package com.fulaan.reportCard.dto;

import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/30.
 */
public class UserRecordDTO {

    private String userId;
    private int status;

    public UserRecordDTO(){

    }

    public UserRecordDTO(String userId){
        this.userId=userId;
        this.status= Constant.ZERO;
    }

    public GroupExamDetailEntry.UserRecordEntry buildEntry(){
        ObjectId uId=null;
        if(StringUtils.isNotBlank(userId)&&ObjectId.isValid(userId)){
            uId=new ObjectId(userId);
        }
        return new GroupExamDetailEntry.UserRecordEntry(uId);
    }

    public UserRecordDTO(GroupExamDetailEntry.UserRecordEntry entry){
        this.userId=entry.getUserId().toString();
        this.status=entry.getStatus();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
