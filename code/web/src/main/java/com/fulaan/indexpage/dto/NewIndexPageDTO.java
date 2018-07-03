package com.fulaan.indexpage.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-28.
 */
public class NewIndexPageDTO {
    private String tag;
    private int cardType;
    private String groupName;
    private String id;
    private String userName;
    private String subject;
    private String avatar;
    private String title;
    private String time;
    private String content;
    private List<Attachement> imageList = new ArrayList<Attachement>();
    private List<VideoDTO> videoList = new ArrayList<VideoDTO>();
    private List<Attachement> voiceList = new ArrayList<Attachement>();
    private List<Attachement> attachements = new ArrayList<Attachement>();
    private String timeExpression;
    private int commentCount;
    private int isRead;
    private int totalReadCount;
    private int readCount;
    private int unReadCount;
    private boolean isOwner;
    private String allContent;

    public NewIndexPageDTO(){

    }

    public NewIndexPageDTO(
            String tag,
            int cardType,
            String groupName,
            String id,
            String userName,
            String subject,
            String avatar,
            String title,
            String time,
            String content,
            List<Attachement> imageList,
            List<VideoDTO> videoList,
            List<Attachement> voiceList,
            List<Attachement> attachements,
            String timeExpression,
            int commentCount,
            int isRead,
            int totalReadCount,
            int readCount,
            int unReadCount,
            boolean isOwner,
            String allContent ){
        this.tag = tag;
        this.cardType = cardType;
        this.groupName = groupName;
        this.id = id;
        this.userName = userName;
        this.subject = subject;
        this.avatar = avatar;
        this.time = time;
        this.title = title;
        this.content = content;
        this.imageList = imageList;
        this.videoList = videoList;
        this.voiceList = voiceList;
        this.attachements = attachements;
        this.timeExpression = timeExpression;
        this.commentCount = commentCount;
        this.isRead = isRead;
        this.totalReadCount = totalReadCount;
        this.readCount = readCount;
        this.unReadCount = unReadCount;
        this.isOwner = isOwner;
        this.allContent = allContent;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public List<VideoDTO> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoDTO> videoList) {
        this.videoList = videoList;
    }

    public List<Attachement> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<Attachement> voiceList) {
        this.voiceList = voiceList;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }

    public String getTimeExpression() {
        return timeExpression;
    }

    public void setTimeExpression(String timeExpression) {
        this.timeExpression = timeExpression;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getTotalReadCount() {
        return totalReadCount;
    }

    public void setTotalReadCount(int totalReadCount) {
        this.totalReadCount = totalReadCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public String getAllContent() {
        return allContent;
    }

    public void setAllContent(String allContent) {
        this.allContent = allContent;
    }
}
