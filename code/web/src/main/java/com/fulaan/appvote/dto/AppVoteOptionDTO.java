package com.fulaan.appvote.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-29.
 */
public class AppVoteOptionDTO {
    private String id;
    private String voteId;
    private String description;
    private int type;
    private int select;
    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private List<Attachement> attachements=new ArrayList<Attachement>();
    private List<Attachement> voiceList=new ArrayList<Attachement>();
    private int count;
    private List<ObjectId> userIdList = new ArrayList<ObjectId>();

    public AppVoteOptionDTO(){

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

    public List<ObjectId> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<ObjectId> userIdList) {
        this.userIdList = userIdList;
    }
}
