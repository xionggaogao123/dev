package com.fulaan.indexpage.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.util.NewStringUtil;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.indexPage.IndexContentEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-04.
 * 首页内容组装
 */
public class IndexContentDTO {

    private String id;
    private String tag;
    private String subject;
    private String userId;
    private String title;
    private String content;
    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private List<Attachement> attachements=new ArrayList<Attachement>();
    private List<Attachement> voiceList=new ArrayList<Attachement>();
    private int commentCount;
    private String groupName;

    private String time;
    private String userName;
    private String avatar;
    private String timeExpression;
    private int isRead;

    private int readCount;
    private int unReadCount;
    private int totalReadCount;

    private boolean isOwner;

    private int cardType;
    public IndexContentDTO(){}


    public IndexContentDTO(
            String subject,
            String title,
            String content,
            List<VideoDTO> videoList,
            List<Attachement> imageList,
            List<Attachement> attachements,
            List<Attachement> voiceList,
            String groupName,
            String userName){
        this.subject=subject;
        this.title=title;
        this.content=content;
        this.videoList=videoList;
        this.imageList=imageList;
        this.attachements=attachements;
        this.voiceList=voiceList;
        this.groupName=groupName;
        this.userName=userName;
    }


    public IndexContentDTO(IndexContentEntry entry){
        this.id=entry.getID().toString();
        this.tag="";
        this.subject=entry.getSubject();
        this.title=entry.getTitle();
        if(entry.getContent()!=null){
            this.content= NewStringUtil.toGoodJsonStr(entry.getContent());
        }else{
            this.content="";
        }
        this.groupName=entry.getGroupName();
        List<VideoEntry> videoEntries=entry.getVideoList();
        for(VideoEntry videoEntry:videoEntries){
            videoList.add(new VideoDTO(videoEntry));
        }
        List<AttachmentEntry> images=entry.getImageList();
        for(AttachmentEntry imageItem:images){
            imageList.add(new Attachement(imageItem));
        }
        List<AttachmentEntry> voiceEntries=entry.getVoiceList();
        for(AttachmentEntry voice:voiceEntries){
            voiceList.add(new Attachement(voice));
        }
        List<AttachmentEntry> attachmentEntries=entry.getAttachmentEntries();
        for(AttachmentEntry attachmentEntry:attachmentEntries){
            attachements.add(new Attachement(attachmentEntry));
        }
        this.commentCount=entry.getCommentCount();
        this.time= DateTimeUtils.convert(entry.getSubmitTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        this.cardType = entry.getContactType();
    }

    public IndexContentEntry buildEntry(String userId,String subjectId,String groupId,String communityId,int watchPermission){
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
        ObjectId cmId=null;
        if(org.apache.commons.lang.StringUtils.isNotBlank(communityId)&&ObjectId.isValid(communityId)){
            cmId=new ObjectId(communityId);
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

        List<AttachmentEntry> voiceEntries=new ArrayList<AttachmentEntry>();
        if(voiceList.size()>0){
            for(Attachement voice:voiceList){
                voiceEntries.add(new AttachmentEntry(voice.getUrl(), voice.getFlnm(),
                        System.currentTimeMillis(),
                        uuId));
            }
        }

        IndexContentEntry entry=new IndexContentEntry(uuId, subject, title, content,
                gId, watchPermission, videoEntries, attachmentEntries,voiceEntries,groupName,userName,subId,cmId,imageEntries);
        return entry;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public int getTotalReadCount() {
        return totalReadCount;
    }

    public void setTotalReadCount(int totalReadCount) {
        this.totalReadCount = totalReadCount;
    }

    public boolean getIsOwner(){
        return isOwner;
    }

    public void setIsOwner(boolean isOwner){
        this.isOwner = isOwner;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
