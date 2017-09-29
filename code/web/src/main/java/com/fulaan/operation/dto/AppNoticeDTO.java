package com.fulaan.operation.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/22.
 */
public class AppNoticeDTO {
    private String id;
    private String userId;
    private String subject;
    private String title;
    private String content;
    private String groupId;
    private List<String> readList= new ArrayList<String>();
    private int watchPermission;
    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private List<Attachement> attachements=new ArrayList<Attachement>();
    private int commentCount;
    private String groupName;
    private String subjectId;

    private String time;
    private String userName;
    private String avatar;
    private String timeExpression;
    private int isRead;

    private int readCount;
    private int unReadCount;


    public AppNoticeDTO(
                        String subjectId,
                        String subject,
                        String title,
                        String content,
                        String groupId,
                        int watchPermission,
                        List<VideoDTO> videoList,
                        List<Attachement> imageList,
                        List<Attachement> attachements,
                        String groupName,
                        String userName){
        this.subjectId=subjectId;
        this.subject=subject;
        this.title=title;
        this.content=content;
        this.groupId=groupId;
        this.watchPermission=watchPermission;
        this.videoList=videoList;
        this.imageList=imageList;
        this.attachements=attachements;
        this.groupName=groupName;
        this.userName=userName;
    }

    public AppNoticeDTO(){

    }

    public AppNoticeEntry buildEntry(){
        ObjectId uuId=null;
        if(org.apache.commons.lang.StringUtils.isNotBlank(userId)&&ObjectId.isValid(userId)){
            uuId=new ObjectId(userId);
        }
        ObjectId subId=null;
        if(org.apache.commons.lang.StringUtils.isNotBlank(subjectId)&&ObjectId.isValid(subjectId)){
            subId=new ObjectId(subjectId);
        }
        ObjectId gId=null;
        if(org.apache.commons.lang.StringUtils.isNotBlank(groupId)&&ObjectId.isValid(groupId)){
            gId=new ObjectId(groupId);
        }
        List<VideoEntry> videoEntries=new ArrayList<VideoEntry>();
        if(videoList.size()>0){
            for(VideoDTO videoDTO:videoList){
                videoEntries.add(new VideoEntry(videoDTO.getVideoUrl(),
                videoDTO.getImageUrl(),System.currentTimeMillis(), uuId));
            }
        }
        List<AttachmentEntry> imageEntries=new ArrayList<AttachmentEntry>();
        if(imageList.size()>0){
            for(Attachement image:imageList){
                imageEntries.add(new AttachmentEntry(image.getUrl(), image.getFlnm(),
                        System.currentTimeMillis(),
                        uuId));
            }
        }

        List<AttachmentEntry> attachmentEntries=new ArrayList<AttachmentEntry>();
        if(attachements.size()>0){
            for(Attachement attachement:attachements){
                attachmentEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        uuId));
            }
        }
        AppNoticeEntry entry=new AppNoticeEntry(uuId, subject, title, content,
                gId, watchPermission, videoEntries, attachmentEntries,groupName,userName,subId,imageEntries);
        return entry;
    }

    public AppNoticeDTO(AppNoticeEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getUserId()==null?"":entry.getUserId().toString();
        this.subjectId=entry.getSubjectId()==null?"":entry.getSubjectId().toString();
        this.subject=entry.getSubject();
        this.title=entry.getTitle();
        this.content=entry.getContent();
        this.groupId=entry.getGroupId()==null?"":entry.getGroupId().toString();
        this.groupName=entry.getGroupName();
        List<ObjectId> entryReaList=entry.getReaList();
        for(ObjectId readItem:entryReaList){
            readList.add(readItem.toString());
        }
        this.watchPermission=entry.getWatchPermission();
        List<VideoEntry> videoEntries=entry.getVideoList();
        for(VideoEntry videoEntry:videoEntries){
            videoList.add(new VideoDTO(videoEntry));
        }
        List<AttachmentEntry> images=entry.getImageList();
        for(AttachmentEntry imageItem:images){
            imageList.add(new Attachement(imageItem));
        }
        this.commentCount=entry.getCommentCount();
        this.time= DateTimeUtils.convert(entry.getSubmitTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getReadList() {
        return readList;
    }

    public void setReadList(List<String> readList) {
        this.readList = readList;
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

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTimeExpression() {
        return timeExpression;
    }

    public void setTimeExpression(String timeExpression) {
        this.timeExpression = timeExpression;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
