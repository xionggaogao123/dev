package com.fulaan.operation.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.util.NewStringUtil;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.operation.AppCommentEntry;
import com.sys.utils.DateTimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业
 * Created by James on 2017/8/25.
 */
@ApiModel
public class AppCommentDTO {
    private String id;
    @ApiModelProperty(value = "描述", required = true)
    private String description;
    private String title;
    @ApiModelProperty(value = "作业提交时间 (yyyy-MM-dd HH:mm:ss)", required = true)
    private String loadTime;
    @ApiModelProperty(value = "作业提交状态:0 已发布    1 定时发布   2 暂不发布", required = true)
    private int status;
    private int writeNumber;
    //  int allWriterNumber,
   // int allLoadNumber,
    private int allWriterNumber;
    private int allLoadNumber;
    private int talkNumber;
    private int loadNumber;
    private int questionNumber;
    @ApiModelProperty(value = "视屏", required = true)
    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();
    @ApiModelProperty(value = "图片", required = true)
    private List<Attachement> imageList=new ArrayList<Attachement>();
    @ApiModelProperty(value = "附件", required = true)
    private List<Attachement> attachements=new ArrayList<Attachement>();
    @ApiModelProperty(value = "语音", required = true)
    private List<Attachement> voiceList=new ArrayList<Attachement>();
    private String subject;
    @ApiModelProperty(value = "学科id", required = true)
    private String subjectId;
    private String adminId;
    private String recipientName;
    private String recipientId;
    @ApiModelProperty(value = "作业发布时间（yyyy-MM-dd）", required = false)
    private String dateTime;
    private String createTime;
    private int month;
    private int number;//回复人数
    private int type;//leixing 1已发送 2 已接受
    private String adminName;//发送人姓名
    private String adminUrl;//发送人图片
    private String sendUser;//孩子名称
    private String comList;//社群组合
    private int isLoad;
    private int showType; //是否展示  0 展示  1不展示

    private String tutorId;//修改助教id
    private String tutorName;//助教姓名
    private String tutorList;//助教和对应社群

    public AppCommentDTO(){

    }
    public AppCommentDTO(AppCommentEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.description = NewStringUtil.toGoodJsonStr(e.getDescription());
            this.title = NewStringUtil.toGoodJsonStr(e.getTitle());
            if(e.getLoadTime()!=0l){
                this.loadTime = DateTimeUtils.getLongToStrTimeTwo(e.getLoadTime());
            }else{
                this.loadTime = "";
            }
            this.status = e.getStatus();
            this.writeNumber = e.getWriteNumber();
            this.allWriterNumber = e.getAllWriterNumber();
            this.allLoadNumber = e.getAllLoadNumber();
            this.talkNumber = e.getTalkNumber();
            this.loadNumber = e.getLoadNumber();
            this.questionNumber = e.getQuestionNumber();
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
            if(videoEntries != null && videoEntries.size()>0){
                for(VideoEntry entry3 : videoEntries){
                    this.videoList.add(new VideoDTO(entry3));
                }
            }

            this.subject = e.getSubject();
            this.subjectId = e.getSubjectId()==null?"":e.getSubjectId().toString();
            this.adminId = e.getAdminId() == null ? "" : e.getAdminId().toString();
            this.recipientName = e.getRecipientName();
            this.recipientId = e.getRecipientId() == null ? "" : e.getRecipientId().toString();
            if(e.getDateTime()!=0l){
                this.dateTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.dateTime = "";
            }
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            this.month = e.getMonth();
            this.showType = e.getShowType();
            this.tutorId = e.getTutorId()==null?"":e.getTutorId().toString();
            this.tutorName="";
        }else{
            new AppCommentDTO();
        }
    }

    public AppCommentEntry buildAddEntry(){
        ObjectId aId=null;
        if(this.getAdminId()!=null&&!"".equals(this.getAdminId())){
            aId=new ObjectId(this.getAdminId());
        }
       ObjectId rId=null;
        if(this.getRecipientId()!=null&&!"".equals(this.getRecipientId())){
            rId=new ObjectId(this.getRecipientId());
        }
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }

        ObjectId tId=null;
        if(this.getTutorId()!=null&&!"".equals(this.getTutorId())){
            tId=new ObjectId(this.getTutorId());
        }
        long lTm = 0l;
        if(this.getLoadTime() != null && this.getLoadTime() != ""){
            lTm = DateTimeUtils.getStrToLongTime(this.getLoadTime(), "yyyy-MM-dd HH:mm");
        }

        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        List<VideoEntry> videoEntries=new ArrayList<VideoEntry>();
        if(videoList.size()>0){
            for(VideoDTO videoDTO:videoList){
                videoEntries.add(new VideoEntry(videoDTO.getVideoUrl(),
                        videoDTO.getImageUrl(),System.currentTimeMillis(), aId));
            }
        }
        List<AttachmentEntry> imageEntries=new ArrayList<AttachmentEntry>();
        if(imageList.size()>0){
            for(Attachement image:imageList){
                imageEntries.add(new AttachmentEntry(image.getUrl(), image.getFlnm(),
                        System.currentTimeMillis(),
                        aId));
            }
        }

        List<AttachmentEntry> attachmentEntries=new ArrayList<AttachmentEntry>();
        if(attachements.size()>0){
            for(Attachement attachement:attachements){
                attachmentEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        aId));
            }
        }
        List<AttachmentEntry> voiceEntries=new ArrayList<AttachmentEntry>();
        if(voiceList.size()>0){
            for(Attachement attachement:voiceList){
                voiceEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        aId));
            }
        }
        AppCommentEntry openEntry =
                new AppCommentEntry(
                        this.description,
                        this.title,
                        lTm,
                        this.status,
                        this.writeNumber,
                        this.allWriterNumber,
                        this.allLoadNumber,
                        this.talkNumber,
                        this.loadNumber,
                        this.questionNumber,
                        imageEntries,
                        voiceEntries,
                        videoEntries,
                        attachmentEntries,
                        this.subject,
                        sId,
                        aId,
                        this.recipientName,
                        rId,
                        this.month,
                        this.showType,
                        dTm,
                        tId);
        return openEntry;

    }
    public AppCommentEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId aId=null;
        if(this.getAdminId()!=null&&!"".equals(this.getAdminId())){
            aId=new ObjectId(this.getAdminId());
        }
        ObjectId rId=null;
        if(this.getRecipientId()!=null&&!"".equals(this.getRecipientId())){
            rId=new ObjectId(this.getRecipientId());
        }
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }

        ObjectId tId=null;
        if(this.getTutorId()!=null&&!"".equals(this.getTutorId())){
            tId=new ObjectId(this.getTutorId());
        }
        long lTm = 0l;
        if(this.getLoadTime() != null && this.getLoadTime() != ""){
            lTm = DateTimeUtils.getStrToLongTime(this.getLoadTime(), "yyyy-MM-dd HH:mm");
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        } List<VideoEntry> videoEntries=new ArrayList<VideoEntry>();
        if(videoList.size()>0){
            for(VideoDTO videoDTO:videoList){
                videoEntries.add(new VideoEntry(videoDTO.getVideoUrl(),
                        videoDTO.getImageUrl(),System.currentTimeMillis(), aId));
            }
        }
        List<AttachmentEntry> imageEntries=new ArrayList<AttachmentEntry>();
        if(imageList.size()>0){
            for(Attachement image:imageList){
                imageEntries.add(new AttachmentEntry(image.getUrl(), image.getFlnm(),
                        System.currentTimeMillis(),
                        aId));
            }
        }
        List<AttachmentEntry> attachmentEntries=new ArrayList<AttachmentEntry>();
        if(attachements.size()>0){
            for(Attachement attachement:attachements){
                attachmentEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        aId));
            }
        }
        List<AttachmentEntry> voiceEntries=new ArrayList<AttachmentEntry>();
        if(voiceList.size()>0){
            for(Attachement attachement:voiceList){
                voiceEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        aId));
            }
        }
        AppCommentEntry openEntry =
                new AppCommentEntry(
                        Id,
                        this.description,
                        this.title,
                        lTm,
                        this.status,
                        this.writeNumber,
                        this.allWriterNumber,
                        this.allLoadNumber,
                        this.talkNumber,
                        this.loadNumber,
                        this.questionNumber,
                        imageEntries,
                        voiceEntries,
                        videoEntries,
                        attachmentEntries,
                        this.subject,
                        sId,
                        aId,
                        this.recipientName,
                        rId,
                        this.month,
                        this.showType,
                        dTm,
                        tId);
        return openEntry;

    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getIsLoad() {
        return isLoad;
    }

    public void setIsLoad(int isLoad) {
        this.isLoad = isLoad;
    }

    public int getAllWriterNumber() {
        return allWriterNumber;
    }

    public void setAllWriterNumber(int allWriterNumber) {
        this.allWriterNumber = allWriterNumber;
    }

    public int getAllLoadNumber() {
        return allLoadNumber;
    }

    public void setAllLoadNumber(int allLoadNumber) {
        this.allLoadNumber = allLoadNumber;
    }

    public String getComList() {
        return comList;
    }

    public void setComList(String comList) {
        this.comList = comList;
    }

    public List<Attachement> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<Attachement> voiceList) {
        this.voiceList = voiceList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWriteNumber() {
        return writeNumber;
    }

    public void setWriteNumber(int writeNumber) {
        this.writeNumber = writeNumber;
    }

    public int getTalkNumber() {
        return talkNumber;
    }

    public void setTalkNumber(int talkNumber) {
        this.talkNumber = talkNumber;
    }

    public int getLoadNumber() {
        return loadNumber;
    }

    public void setLoadNumber(int loadNumber) {
        this.loadNumber = loadNumber;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getTutorList() {
        return tutorList;
    }

    public void setTutorList(String tutorList) {
        this.tutorList = tutorList;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }
}
