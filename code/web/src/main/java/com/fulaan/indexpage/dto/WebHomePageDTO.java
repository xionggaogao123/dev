package com.fulaan.indexpage.dto;

import com.fulaan.pojo.Attachement;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/18.
 */
public class WebHomePageDTO {

    private String id;
    private String groupExamDetailId;
    private int type;
    private String title;
    private String content;
    private String groupName;
    private String avatar;
    private String userName;
    private String subjectName;
    //成绩单
    private String examTime;
    private String examTypeName;
    private int recordScoreType;
    private String childUserId;
    private String childUserName;
    private String contactId;
    private boolean isOwner;

    private int commentCount;
    private int signedCount;
    private int totalCount;

    private String timeExpression;

    private String submitWorkTime;
    //附件
    private List<Attachement> attachementList=new ArrayList<Attachement>();
    private List<Attachement> imageList=new ArrayList<Attachement>();

    private boolean isSign;

    private double avgScore;//平均分

    private double aPercent;//a率

    private int status;

    private long submitTime;

    public WebHomePageDTO(){

    }



    public WebHomePageDTO(GroupExamDetailEntry entry){
        this.title=entry.getExamName();
        this.examTime=DateTimeUtils.convert(entry.getExamTime(),DateTimeUtils.DATE_YYYY_MM_DD);
        this.signedCount=entry.getSignedCount();
        this.totalCount=entry.getSignCount();
        this.recordScoreType=entry.getRecordScoreType();
        this.submitTime = entry.getSubmitTime();
    }

    public WebHomePageDTO(AppNoticeEntry appNoticeEntry){
        this.id=appNoticeEntry.getID().toString();
        this.title=appNoticeEntry.getTitle();
        this.content=appNoticeEntry.getContent();
        this.commentCount=appNoticeEntry.getCommentCount();
        List<AttachmentEntry> imgs=appNoticeEntry.getImageList();
        for(AttachmentEntry attachmentEntry:imgs){
            imageList.add(new Attachement(attachmentEntry));
        }
        List<AttachmentEntry> attachmentEntries=appNoticeEntry.getAttachmentEntries();
        for(AttachmentEntry attachmentEntry:attachmentEntries){
            attachementList.add(new Attachement(attachmentEntry));
        }
    }

    public WebHomePageDTO(AppCommentEntry appCommentEntry){
        this.id=appCommentEntry.getID().toString();
        this.title=appCommentEntry.getTitle();
        this.content=appCommentEntry.getDescription();
        this.commentCount=appCommentEntry.getTalkNumber();
        this.submitWorkTime= DateTimeUtils.convert(appCommentEntry.getDateTime(),
                DateTimeUtils.DATE_YYYY_MM_DD);
        List<AttachmentEntry> imgs=appCommentEntry.getImageList();
        for(AttachmentEntry attachmentEntry:imgs){
            imageList.add(new Attachement(attachmentEntry));
        }
        List<AttachmentEntry> attachmentEntries=appCommentEntry.getAttachmentEntries();
        for(AttachmentEntry attachmentEntry:attachmentEntries){
            attachementList.add(new Attachement(attachmentEntry));
        }

    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getChildUserId() {
        return childUserId;
    }

    public void setChildUserId(String childUserId) {
        this.childUserId = childUserId;
    }

    public String getChildUserName() {
        return childUserName;
    }

    public void setChildUserName(String childUserName) {
        this.childUserName = childUserName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public double getaPercent() {
        return aPercent;
    }

    public void setaPercent(double aPercent) {
        this.aPercent = aPercent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getExamTypeName() {
        return examTypeName;
    }

    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
    }

    public int getRecordScoreType() {
        return recordScoreType;
    }

    public void setRecordScoreType(int recordScoreType) {
        this.recordScoreType = recordScoreType;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getSignedCount() {
        return signedCount;
    }

    public void setSignedCount(int signedCount) {
        this.signedCount = signedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getTimeExpression() {
        return timeExpression;
    }

    public void setTimeExpression(String timeExpression) {
        this.timeExpression = timeExpression;
    }

    public String getSubmitWorkTime() {
        return submitWorkTime;
    }

    public void setSubmitWorkTime(String submitWorkTime) {
        this.submitWorkTime = submitWorkTime;
    }

    public List<Attachement> getAttachementList() {
        return attachementList;
    }

    public void setAttachementList(List<Attachement> attachementList) {
        this.attachementList = attachementList;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }



    public String getGroupExamDetailId() {
        return groupExamDetailId;
    }



    public void setGroupExamDetailId(String groupExamDetailId) {
        this.groupExamDetailId = groupExamDetailId;
    }
    
    
}
