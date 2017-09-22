package com.fulaan.operation.dto;

import com.pojo.operation.AppCommentEntry;
import com.sys.utils.DateTimeUtils;
import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 作业
 * Created by James on 2017/8/25.
 */
public class AppCommentDTO {
    private String id;
    @ApiModelProperty(value = "描述", required = true)
    private String description;
    @ApiModelProperty(value = "作业提交时间 (yyyy-MM-dd HH:mm:ss)", required = true)
    private String loadTime;
    @ApiModelProperty(value = "作业提交状态", required = true)
    private int status;
    private int writeNumber;
    private int talkNumber;
    private int loadNumber;
    private int questionNumber;
    @ApiModelProperty(value = "图片url", required = true)
    private List<String> imageUrl;
    @ApiModelProperty(value = "学科名", required = true)
    private String subject;
    private String adminId;
    private String recipientName;
    private String recipientId;
    @ApiModelProperty(value = "作业发布时间（yyyy-MM-dd）", required = false)
    private String dateTime;
    private String createTime;
    private int month;
    private int number;//回复人数
    private int type;//leixing
    private String adminName;//发送人姓名
    private String adminUrl;//发送人图片
    private String sendUser;//孩子名称

    public AppCommentDTO(){

    }
    public AppCommentDTO(AppCommentEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.description = e.getDescription();
            if(e.getLoadTime()!=0l){
                this.loadTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.loadTime = "";
            }
            this.status = e.getStatus();
            this.writeNumber = e.getWriteNumber();
            this.talkNumber = e.getTalkNumber();
            this.loadNumber = e.getLoadNumber();
            this.questionNumber = e.getQuestionNumber();
            this.imageUrl = e.getImageUrl();
            this.subject = e.getSubject();
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
        long lTm = 0l;
        if(this.getLoadTime() != null && this.getLoadTime() != ""){
            lTm = DateTimeUtils.getStrToLongTime(this.getLoadTime());
        }

        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        AppCommentEntry openEntry =
                new AppCommentEntry(
                        this.description,
                        lTm,
                        this.status,
                        this.writeNumber,
                        this.talkNumber,
                        this.loadNumber,
                        this.questionNumber,
                        this.imageUrl,
                        this.subject,
                        aId,
                        this.recipientName,
                        rId,
                        this.month,
                        dTm);
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
        long lTm = 0l;
        if(this.getLoadTime() != null && this.getLoadTime() != ""){
            lTm = DateTimeUtils.getStrToLongTime(this.getLoadTime());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        AppCommentEntry openEntry =
                new AppCommentEntry(
                        Id,
                        this.description,
                        lTm,
                        this.status,
                        this.writeNumber,
                        this.talkNumber,
                        this.loadNumber,
                        this.questionNumber,
                        this.imageUrl,
                        this.subject,
                        aId,
                        this.recipientName,
                        rId,
                        this.month,
                        dTm);
        return openEntry;

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

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
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
}
