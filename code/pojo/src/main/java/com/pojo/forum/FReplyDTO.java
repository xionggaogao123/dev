package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/5/31.
 */
public class FReplyDTO {

    private String fReplyId;
    private String postId;
    private String postTitle;
    private String replyImage;
    private String replyComment;
    private String postSectionId;
    private Long   time;
    private String platform;
    private String personName;
    private String personId;

    public FReplyDTO(){}

    public FReplyDTO(FReplyEntry fReplyEntry){
        this.fReplyId=fReplyEntry.getID().toString();
        this.postId=fReplyEntry.getPostId().toString();
        this.postTitle=fReplyEntry.getPostTitle();
        this.replyImage=fReplyEntry.getReplyImage();
        this.replyComment=fReplyEntry.getReplyComment();
        this.postSectionId=fReplyEntry.getPostSectionId().toString();
        this.time=fReplyEntry.getTime();
        this.platform=fReplyEntry.getPlatform();
        this.personName=fReplyEntry.getPersonName();
        this.personId=fReplyEntry.getPersonId().toString();
    }
    public FReplyEntry exportEntry() {
        FReplyEntry fReplyEntry=new FReplyEntry();
        if(!fReplyId.equals("")){
            fReplyEntry.setID(new ObjectId(fReplyId));
        }
        if(!postId.equals("")){
            fReplyEntry.setPostId(new ObjectId((postId)));
        }
        fReplyEntry.setPostTitle(postTitle);
        fReplyEntry.setReplyImage(replyImage);
        fReplyEntry.setReplyComment(replyComment);
        fReplyEntry.setPlatform(platform);
        fReplyEntry.setTime(time);
        fReplyEntry.setPersonName(personName);
        if(!postSectionId.equals("")){
            fReplyEntry.setPostSectionId(new ObjectId((postSectionId)));
        }
        if(!personId.equals("")){
            fReplyEntry.setPersonId(new ObjectId(personId));
        }
        return fReplyEntry;
    }

    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }

    public String getReplyImage() {
        return replyImage;
    }

    public void setReplyImage(String replyImage) {
        this.replyImage = replyImage;
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

    public String getfReplyId() {
        return fReplyId;
    }

    public void setfReplyId(String fReplyId) {
        this.fReplyId = fReplyId;
    }

    public String getPostSectionId() {
        return postSectionId;
    }

    public void setPostSectionId(String postSectionId) {
        this.postSectionId = postSectionId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
