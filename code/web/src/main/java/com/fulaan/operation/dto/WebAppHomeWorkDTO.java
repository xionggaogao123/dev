package com.fulaan.operation.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taotao.chan on 2018年8月15日13:41:08
 */
public class WebAppHomeWorkDTO {
    private String id;
    private String subjectId;
    private String title;
    private String description;
    private String subject;
    private int status;
    private String comList;
    private int showType; //是否展示  0 展示  1不展示

    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();
    private List<Attachement> voiceList=new ArrayList<Attachement>();
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private List<Attachement> attachements=new ArrayList<Attachement>();
    /*  "id": "",
        "subjectId":this.selectedSubjectId,
        "title":tit,
        "description":contentValue,
        "subject":this.selectedSubjectName,
        "videoList":this.videoList,
        "voiceList":this.voiceList,
        "imageList":this.imageList,
        "attachements":this.attachements,
        "status":status,
        "comList":this.groupOfCommunityDTOs*/
    public AppCommentDTO getAppCommentDTO(WebAppHomeWorkDTO dto){
        AppCommentDTO appDto = new AppCommentDTO();
        if(dto != null){
            appDto.setId(dto.getId());
            appDto.setSubjectId(dto.getSubjectId());
            appDto.setTitle(dto.getTitle());
            appDto.setDescription(dto.getDescription());
            appDto.setSubject(dto.getSubject());
            appDto.setStatus(dto.getStatus());
            appDto.setComList(dto.getComList());
            appDto.setVideoList(dto.getVideoList());
            appDto.setImageList(dto.getImageList());
            appDto.setVoiceList(dto.getVoiceList());
            appDto.setAttachements(dto.getAttachements());
            appDto.setShowType(dto.getShowType());
        }
        return appDto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComList() {
        return comList;
    }

    public void setComList(String comList) {
        this.comList = comList;
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

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }
}
