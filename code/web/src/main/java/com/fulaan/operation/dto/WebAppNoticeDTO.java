package com.fulaan.operation.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/5.
 */
public class WebAppNoticeDTO {


    private String subjectId;
    private String title;
    private String content;
    private String subject;
    private int watchPermission;


    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();
    private List<Attachement> voiceList=new ArrayList<Attachement>();
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private List<Attachement> attachements=new ArrayList<Attachement>();
    private List<GroupOfCommunityDTO> groupOfCommunityDTOs=new ArrayList<GroupOfCommunityDTO>();


    public WebAppNoticeDTO(){

    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getWatchPermission() {
        return watchPermission;
    }

    public void setWatchPermission(int watchPermission) {
        this.watchPermission = watchPermission;
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

    public List<GroupOfCommunityDTO> getGroupOfCommunityDTOs() {
        return groupOfCommunityDTOs;
    }

    public void setGroupOfCommunityDTOs(List<GroupOfCommunityDTO> groupOfCommunityDTOs) {
        this.groupOfCommunityDTOs = groupOfCommunityDTOs;
    }
}
