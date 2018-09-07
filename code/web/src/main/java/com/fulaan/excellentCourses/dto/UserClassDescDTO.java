package com.fulaan.excellentCourses.dto;

import com.pojo.excellentCourses.UserClassDescEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by James on 2018-09-06.
 */
public class UserClassDescDTO {
    /* ObjectId userId,
            ObjectId contactId,
            ObjectId parentId,
            int currentTime,
            int number,
            int allTime,
            long startTime,
            long endTime,
            int status*/
    private String id;
    private String userId;
    private String contactId;
    private int currentTime;
    private int number;
    private int allTime;
    private String startTime;
    private String endTime;
    private int status;

    public UserClassDescDTO(){

    }

    public UserClassDescDTO(UserClassDescEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.contactId = e.getContactId()==null?"":e.getContactId().toString();
            this.currentTime = e.getCurrentTime();
            this.number = e.getNumber();
            this.allTime = e.getAllTime();
            if(e.getStartTime()!=0l){
                this.startTime = DateTimeUtils.getLongToStrTimeTwo(e.getStartTime());
            }else{
                this.startTime = "";
            }
            if(e.getEndTime()!=0l){
                this.endTime = DateTimeUtils.getLongToStrTimeTwo(e.getEndTime());
            }else{
                this.endTime = "";
            }
        }else{
            new HourClassDTO();
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

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAllTime() {
        return allTime;
    }

    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
