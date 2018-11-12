package com.fulaan.appvote.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import com.pojo.appvote.AppVoteOptionEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-29.
 */
public class AppVoteOptionDTO {
    private String id;
    private String voteId;
    private String description;
    private String userId;
    private String userNames;
    private int type;
    private int select;
    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private List<Attachement> attachements=new ArrayList<Attachement>();
    private List<Attachement> voiceList=new ArrayList<Attachement>();
    private int count;
    private double percent;

    private int isSelect;  // 0 未选择  1  已选择

    public AppVoteOptionDTO(){

    }
    public AppVoteOptionDTO(AppVoteOptionEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.voteId = e.getVoteId()==null?"":e.getVoteId().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.description = e.getDescription();
            this.type = e.getType();
            this.select = e.getSelect();
            List<AttachmentEntry> attachmentEntries = e.getImageList();
            if(attachmentEntries != null && attachmentEntries.size()>0){
                for(AttachmentEntry entry : attachmentEntries){
                    this.imageList.add(new Attachement(entry));
                }
            }
            List<AttachmentEntry> attachmentEntries2 = e.getAttachmentEntries();
            if(attachmentEntries2 != null && attachmentEntries2.size()>0){
                for(AttachmentEntry entry2 : attachmentEntries2){
                    this.attachements.add(new Attachement(entry2));
                }
            }
            List<AttachmentEntry> attachmentEntries3 = e.getVoiceList();
            if(attachmentEntries3 != null && attachmentEntries3.size()>0){
                for(AttachmentEntry entry3 : attachmentEntries3){
                    this.voiceList.add(new Attachement(entry3));
                }
            }
            List<VideoEntry> videoEntries = e.getVideoList();
            if(videoEntries != null && videoEntries.size()>0) {
                for (VideoEntry entry3 : videoEntries) {
                    this.videoList.add(new VideoDTO(entry3));
                }
            }
            this.count = e.getCount();
        }else{
            new AppVoteOptionDTO();
        }
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public List<VideoDTO> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoDTO> videoList) {
        this.videoList = videoList;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }

    public List<Attachement> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<Attachement> voiceList) {
        this.voiceList = voiceList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
