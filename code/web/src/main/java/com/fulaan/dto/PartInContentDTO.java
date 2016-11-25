package com.fulaan.dto;

import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.ShareContent;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.PartInContentEntry;
import com.pojo.fcommunity.VideoEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/10/24.
 */
public class PartInContentDTO {

    private String partInContentId;
    private String userId;
    private String information;
    private String userName;
    private String avator;
    private String nickName;
    private String time;

    private int zan;

    private List<String> imageList = new ArrayList<String>();
    private List<Attachement> attachmentList = new ArrayList<Attachement>();
    private List<VideoDTO> videoList = new ArrayList<VideoDTO>();

    //学习用品
    private String shareUrl;
    private String shareImage;
    private String shareTitle;
    private String sharePrice;
    private String shareCommend;
    //类型
    private int type;

    private int ownerZan;
    private List<String> zanUserIds;

    //批阅
    private int mark;

    //isManager(判断用户是不是社长或副社长)
    private boolean isManager;

    public PartInContentDTO(PartInContentEntry partInContent) {
        this.partInContentId = partInContent.getID().toString();
        this.userId = partInContent.getUserId().toString();
        this.information = partInContent.getInformation();
        this.attachmentList = getAttachments(partInContent.getAttachementList());
        this.imageList = partInContent.getImageList();
        this.videoList = getVideos(partInContent.getVedioList());
        this.shareUrl = partInContent.getShareUrl();
        this.shareImage = partInContent.getShareImage();
        this.shareTitle = partInContent.getShareTitle();
        this.sharePrice = partInContent.getSharePrice();
        this.shareCommend = partInContent.getShareCommend();
        this.type = partInContent.getType();
        this.zan = partInContent.getZan();
        this.zanUserIds = getZanUserIds(partInContent.getZanList());
        this.mark = partInContent.getMark();
    }

    private List<String> getZanUserIds(List<ObjectId> zanUserIds) {
        List<String> retList = new ArrayList<String>();
        for (ObjectId objectId : zanUserIds) {
            retList.add(objectId.toString());
        }
        return retList;
    }

    private List<VideoDTO> getVideos(List<VideoEntry> videoEntries) {
        List<VideoDTO> videoDTOs = new ArrayList<VideoDTO>();
        for (VideoEntry entry : videoEntries) {
            videoDTOs.add(new VideoDTO(entry));
        }
        return videoDTOs;
    }

    private List<Attachement> getAttachments(List<AttachmentEntry> entries) {
        List<Attachement> attachements = new ArrayList<Attachement>();
        for (AttachmentEntry entry : entries) {
            attachements.add(new Attachement(entry));
        }
        return attachements;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<Attachement> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachement> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<VideoDTO> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoDTO> videoList) {
        this.videoList = videoList;
    }

    public String getPartInContentId() {
        return partInContentId;
    }

    public void setPartInContentId(String partInContentId) {
        this.partInContentId = partInContentId;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(String sharePrice) {
        this.sharePrice = sharePrice;
    }

    public String getShareCommend() {
        return shareCommend;
    }

    public void setShareCommend(String shareCommend) {
        this.shareCommend = shareCommend;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOwnerZan() {
        return ownerZan;
    }

    public void setOwnerZan(int ownerZan) {
        this.ownerZan = ownerZan;
    }

    public List<String> getZanUserIds() {
        return zanUserIds;
    }

    public void setZanUserIds(List<String> zanUserIds) {
        this.zanUserIds = zanUserIds;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
