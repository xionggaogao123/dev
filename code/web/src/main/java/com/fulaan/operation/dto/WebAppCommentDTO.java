package com.fulaan.operation.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/12/7.
 */
public class WebAppCommentDTO {
    private String id;
    private int isPublic;
    private String subjectId;
    private String title;
    private String description;
    private String subject;
    private int status;
    private String comList;

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
    public AppCommentDTO getAppCommentDTO(WebAppCommentDTO dto){
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
            appDto.setIsPublic(dto.getIsPublic());
        }
        return appDto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
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
}
