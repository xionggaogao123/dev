package com.pojo.forum;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/21.
 */
public class FRecordDTO {
    private String fRecordId;
    private String postId;
    private String postTitle;
    private Long   time;
    private String personId; //处理人
    private String userId; //处理对象
    private int    logRecord;
    private String postSectionId;
    private int    logScan;

    //显示内容
    private String imageSrc;
    private String timeText;
    private String content;

    public FRecordDTO(){}

    public FRecordDTO(FRecordEntry fRecordEntry){
        this.fRecordId=fRecordEntry.getID().toString();
        if(null !=fRecordEntry.getPostId()){
            this.postId=fRecordEntry.getPostId().toString();
        }else{
            this.postId="";
        }
        if(null !=fRecordEntry.getPostSectionId()){
            this.postSectionId=fRecordEntry.getPostSectionId().toString();
        }else{
            this.postSectionId="";
        }
        this.postTitle=fRecordEntry.getPostTitle();
        this.time=fRecordEntry.getTime();
        this.logRecord=fRecordEntry.getLogRecord();
        this.logScan=fRecordEntry.getLogScan();
        if(null !=fRecordEntry.getPersonId()){
            this.personId=fRecordEntry.getPersonId().toString();
        }else{
            this.personId="";
        }
        if(null !=fRecordEntry.getUserId()){
            this.userId=fRecordEntry.getUserId().toString();
        }else{
            this.userId="";
        }
    }
    public FRecordEntry exportEntry() {
        FRecordEntry fRecordEntry=new FRecordEntry();
        if(fRecordId != null &&!fRecordId.equals("")){
            fRecordEntry.setID(new ObjectId(fRecordId));
        }
        if(postId != null&& !postId.equals("")){
            fRecordEntry.setPostId(new ObjectId((postId)));
        }
        if(postSectionId != null&& !postSectionId.equals("")){
            fRecordEntry.setPostSectionId(new ObjectId((postSectionId)));
        }
        fRecordEntry.setPostTitle(postTitle);
        fRecordEntry.setTime(time);
        fRecordEntry.setLogRecord(logRecord);
        fRecordEntry.setLogScan(logScan);
        if(personId !=null && !personId.equals("")){
            fRecordEntry.setPersonId(new ObjectId(personId));
        }
        if(userId !=null && !userId.equals("")){
            fRecordEntry.setUserId(new ObjectId(userId));
        }
        return fRecordEntry;
    }

    public String getfRecordId() {
        return fRecordId;
    }

    public void setfRecordId(String fRecordId) {
        this.fRecordId = fRecordId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public int getLogRecord() {
        return logRecord;
    }

    public void setLogRecord(int logRecord) {
        this.logRecord = logRecord;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostSectionId() {
        return postSectionId;
    }

    public void setPostSectionId(String postSectionId) {
        this.postSectionId = postSectionId;
    }

    public int getLogScan() {
        return logScan;
    }

    public void setLogScan(int logScan) {
        this.logScan = logScan;
    }
}
