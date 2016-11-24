package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/5/31.
 */
public class FPostDTO {

    private String fpostId;
    private String personId;
    private String personName;
    private String postTitle;
    private String postSectionId;
    private String postSectionName;
    private Long   time;
    private Long   updateTime;
    private String postSectionLevelId;
    private String postSectionLevelName;
    private int    cream;
    private int    top;
    private int    scanCount;
    private int    commentCount;
    private String image;
    private int    postSectionCount;
    private String comment;
    private int    draught;
    private int    classify;
    private String platform;

    public FPostDTO(){}

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public FPostDTO(FPostEntry fPostEntry){
        this.fpostId=fPostEntry.getID().toString();
        this.personId=fPostEntry.getPersonId().toString();
        this.personName=fPostEntry.getPersonName();
        this.postTitle=fPostEntry.getPostTitle();
        this.postSectionId=fPostEntry.getPostSectionId().toString();
        this.postSectionName=fPostEntry.getPostSectionName();
        this.time=fPostEntry.getTime();
        this.updateTime=fPostEntry.getUpdateTime();
        this.postSectionLevelId=fPostEntry.getPostSectionLevelId().toString();
        this.postSectionLevelName=fPostEntry.getPostSectionLevelName();
        this.cream=fPostEntry.getCream();
        this.top=fPostEntry.getTop();
        this.scanCount=fPostEntry.getScanCount();
        this.commentCount=fPostEntry.getCommentCount();
        this.image=fPostEntry.getPostSectionImage();
        this.postSectionCount=fPostEntry.getPostSectionCount();
        this.comment=fPostEntry.getComment();
        this.draught=fPostEntry.getDraught();
        this.classify=fPostEntry.getClassify();
        this.platform=fPostEntry.getPlatform();
    }

    public FPostEntry exportEntry(){
        FPostEntry fPostEntry=new FPostEntry();
        if(!fpostId.equals("")){
            fPostEntry.setID(new ObjectId(fpostId));
        }
        if(!personId.equals("")){
            fPostEntry.setPersonId(new ObjectId(personId));
        }
        fPostEntry.setPersonName(personName);
        fPostEntry.setPostTitle(postTitle);
        if(!postSectionId.equals("")){
            fPostEntry.setPostSectionId(new ObjectId(postSectionId));
        }
        fPostEntry.setPostSectionName(postSectionName);
        fPostEntry.setTime(time);
        fPostEntry.setUpdateTime(updateTime);
        if(!postSectionLevelId.equals("")){
            fPostEntry.setPostSectionLevelId(new ObjectId(postSectionLevelId));
        }
        fPostEntry.setPostSectionLevelName(postSectionLevelName);
        fPostEntry.setCream(cream);
        fPostEntry.setTop(top);
        fPostEntry.setScanCount(scanCount);
        fPostEntry.setCommentCount(commentCount);
        fPostEntry.setPostSectionImage(image);
        fPostEntry.setPostSectionCount(postSectionCount);
        fPostEntry.setComment(comment);
        fPostEntry.setDraught(draught);
        fPostEntry.setClassify(classify);
        fPostEntry.setPlatform(platform);
        return fPostEntry;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFpostId() {
        return fpostId;
    }

    public void setFpostId(String fpostId) {
        this.fpostId = fpostId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPostSectionName() {
        return postSectionName;
    }

    public void setPostSectionName(String postSectionName) {
        this.postSectionName = postSectionName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public int getDraught() {
        return draught;
    }

    public void setDraught(int draught) {
        this.draught = draught;
    }

    public int getPostSectionCount() {
        return postSectionCount;
    }

    public void setPostSectionCount(int postSectionCount) {
        this.postSectionCount = postSectionCount;
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

    public String getPostSectionLevelName() {
        return postSectionLevelName;
    }

    public void setPostSectionLevelName(String postSectionLevelName) {
        this.postSectionLevelName = postSectionLevelName;
    }

    public int getCream() {
        return cream;
    }

    public void setCream(int cream) {
        this.cream = cream;
    }

    public String getPostSectionLevelId() {
        return postSectionLevelId;
    }

    public void setPostSectionLevelId(String postSectionLevelId) {
        this.postSectionLevelId = postSectionLevelId;
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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
