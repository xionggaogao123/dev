package com.fulaan.pojo;

import com.fulaan.dto.VideoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/10/25.
 * Message
 */
public class CommunityMessage {

    private String communityId;
    private String title;
    private String content;
    private int type;
    private String shareUrl;
    private String shareImage;
    private String shareTitle;
    private String sharePrice;
    private List<Attachement> attachements = new ArrayList<Attachement>();
    private List<Attachement> vedios = new ArrayList<Attachement>();
    private List<Attachement> images = new ArrayList<Attachement>();
    private String voteContent;
    private int voteMaxCount;
    private String voteDeadTime;
    private int voteType;


    public CommunityMessage(String communityId,
                            String title,
                            String content,
                            int type,
                            String shareUrl,
                            String shareImage,
                            String shareTitle,
                            String sharePrice,
                            List<Attachement> attachements,
                            List<Attachement> vedios,
                            List<VideoDTO> videoDTOs,
                            List<Attachement> images,
                            String voteContent,
                            int voteMaxCount,
                            String voteDeadTime,
                            int voteType){
        this.communityId=communityId;
        this.title=title;
        this.content=content;
        this.type=type;
        this.shareUrl=shareUrl;
        this.shareImage=shareImage;
        this.shareTitle=shareTitle;
        this.sharePrice=sharePrice;
        this.attachements=attachements;
        this.vedios=vedios;
        this.videoDTOs = videoDTOs;
        this.images=images;
        this.voteContent=voteContent;
        this.voteMaxCount=voteMaxCount;
        this.voteDeadTime=voteDeadTime;
        this.voteType=voteType;
    }

    public CommunityMessage(){

    }

    //视频
    private List<VideoDTO> videoDTOs = new ArrayList<VideoDTO>();

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }

    public List<Attachement> getVedios() {
        return vedios;
    }

    public void setVedios(List<Attachement> vedios) {
        this.vedios = vedios;
    }

    public List<Attachement> getImages() {
        return images;
    }

    public void setImages(List<Attachement> images) {
        this.images = images;
    }

    public String getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(String voteContent) {
        this.voteContent = voteContent;
    }

    public int getVoteMaxCount() {
        return voteMaxCount;
    }

    public void setVoteMaxCount(int voteMaxCount) {
        this.voteMaxCount = voteMaxCount;
    }

    public int getVoteType() {
        return voteType;
    }

    public void setVoteType(int voteType) {
        this.voteType = voteType;
    }

    public String getVoteDeadTime() {
        return voteDeadTime;
    }

    public void setVoteDeadTime(String voteDeadTime) {
        this.voteDeadTime = voteDeadTime;
    }

    public List<VideoDTO> getVideoDTOs() {
        return videoDTOs;
    }

    public void setVideoDTOs(List<VideoDTO> videoDTOs) {
        this.videoDTOs = videoDTOs;
    }
}
