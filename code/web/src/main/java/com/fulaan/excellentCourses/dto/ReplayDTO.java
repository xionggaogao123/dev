package com.fulaan.excellentCourses.dto;

/**
 * Created by James on 2018-07-15.
 */
public class ReplayDTO {

    private String id;
    private String liveId;
    private String roomId;
    private String recordVideoId;
    private String userId;
    private String userName;
    private String password;
    private String startTime;
    private String stopTime;
    private int recordStatus;
    private String replayUrl;
    private String teacherName;

    public ReplayDTO(){

    }

    public ReplayDTO(String id,
                     String liveId,
                     String roomId,
                     String recordVideoId,
                     String userId,
                     String userName,
                     String password,
                     String startTime,
                     String stopTime,
                     int recordStatus,
                     String replayUrl,
                     String teacherName){
        this.id = id;
        this.liveId = liveId;
        this.roomId = roomId;
        this.recordVideoId = recordVideoId;
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        if(startTime!=null && !startTime.equals("")){
            this.startTime = startTime.substring(0,16);
        }else{
            this.startTime = "";
        }
        if(stopTime!=null && !stopTime.equals("")){
            this.stopTime = stopTime.substring(0,16);
        }else{
            this.stopTime = "";
        }
        this.recordStatus = recordStatus;
        this.replayUrl = replayUrl;
        this.teacherName = teacherName;
    }

    public ReplayDTO(String id,
                     String liveId,
                     String roomId,
                     String recordVideoId,
                     String userId,
                     String userName,
                     String password,
                     String startTime,
                     String stopTime,
                     int recordStatus,
                     String replayUrl){
        this.id = id;
        this.liveId = liveId;
        this.roomId = roomId;
        this.recordVideoId = recordVideoId;
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.recordStatus = recordStatus;
        this.replayUrl = replayUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRecordVideoId() {
        return recordVideoId;
    }

    public void setRecordVideoId(String recordVideoId) {
        this.recordVideoId = recordVideoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getReplayUrl() {
        return replayUrl;
    }

    public void setReplayUrl(String replayUrl) {
        this.replayUrl = replayUrl;
    }


    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
