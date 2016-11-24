package com.pojo.forum;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/30.
 */
public class FCollectionDTO {
    private String id;
    private String userId;
    private String postSectionId;
    private String userAvatar;
    private String userName;
    private int type;// 0:帖子 1:版块

    private String sectionId;
    private String sectionName;

    //帖子专用字段
    private String postId;
    private String postTitle;
    private int scanCount;
    private int commentCount;
    private String from;

    //版块专用字段
    private int count;//每天发帖数
    private int totalCount;//帖子总数
    private int focusCount;//关注人数
    //收藏时间
    private String time;
    private String personId;



    public FCollectionDTO(){}


    public FCollectionDTO(FCollectionEntry fCollectionEntry){

        this.id=fCollectionEntry.getID().toString();
        this.userId=fCollectionEntry.getUserId().toString();
        this.type=fCollectionEntry.getType();
        this.postSectionId=fCollectionEntry.getPostSectionId().toString();
    }

    public FCollectionEntry exportEntry(){
        FCollectionEntry fCollectionEntry=new FCollectionEntry();
        if(id != null && !id.equals("")){
            fCollectionEntry.setID(new ObjectId(id));
        }
        if(!userId.equals("")){
            fCollectionEntry.setUserId(new ObjectId(userId));
        }
        if(!postSectionId.equals("")){
            fCollectionEntry.setPostSectionId(new ObjectId(postSectionId));
        }
        fCollectionEntry.setType(type);
        return fCollectionEntry;
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

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(int focusCount) {
        this.focusCount = focusCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostSectionId() {
        return postSectionId;
    }

    public void setPostSectionId(String postSectionId) {
        this.postSectionId = postSectionId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
