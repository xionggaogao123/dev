package com.fulaan.appactivity.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.pojo.Attachement;
import com.pojo.appactivity.AppActivityEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/27.
 */
public class AppActivityDTO {

    private String id;
    private String subjectId;
    private String userId;
    private String subjectName;
    private String title;
    private String content;
    private String groupId;
    private String communityId;
    private String groupName;
    private List<Attachement> imageList = new ArrayList<Attachement>();
    private List<VideoDTO> videoDTOs = new ArrayList<VideoDTO>();
    private List<GroupOfCommunityDTO> groupOfCommunityDTOs = new ArrayList<GroupOfCommunityDTO>();
    private int partInCount;
    private int visiblePermission;

    private String userName;
    private String avatar;
    private boolean isOwner;
    private String submitTime;
    private boolean isPartIn;
    private int totalCount;

    private int manageDelete;

    public AppActivityDTO(){

    }


    public AppActivityDTO(
            String subjectId, String userId,
            String subjectName,
            String title,
            String content,
            List<Attachement> attachements,
            List<VideoDTO> videoDTOList,
            int visiblePermission,
            String groupId,
            String communityId,
            String groupName
    ){
        this.subjectId = subjectId;
        this.userId = userId;
        this.subjectName = subjectName;
        this.title = title;
        this.content = content;
        this.imageList.addAll(attachements);
        this.videoDTOs.addAll(videoDTOList);
        this.visiblePermission = visiblePermission;
        this.groupId=groupId;
        this.communityId=communityId;
        this.groupName=groupName;
    }


    public AppActivityEntry buildEntry() {
        ObjectId suId = null;
        if (StringUtils.isNotEmpty(subjectId) && ObjectId.isValid(subjectId)) {
            suId = new ObjectId(subjectId);
        }
        ObjectId uId = null;
        if (StringUtils.isNotEmpty(userId) && ObjectId.isValid(userId)) {
            uId = new ObjectId(userId);
        }
        ObjectId gId = null;
        if (StringUtils.isNotEmpty(groupId) && ObjectId.isValid(groupId)) {
            gId = new ObjectId(groupId);
        }
        ObjectId cid = null;
        if (StringUtils.isNotEmpty(communityId) && ObjectId.isValid(communityId)) {
            cid = new ObjectId(communityId);
        }
        List<AttachmentEntry> ims = new ArrayList<AttachmentEntry>();
        for (Attachement item : imageList) {
            ims.add(new AttachmentEntry(item.getUrl(), item.getFlnm(), System.currentTimeMillis(), uId));
        }
        List<VideoEntry> vs = new ArrayList<VideoEntry>();
        for (VideoDTO item : videoDTOs) {
            vs.add(new VideoEntry(item.getVideoUrl(), item.getImageUrl(), System.currentTimeMillis(), uId));
        }
        return new AppActivityEntry(suId,
                uId,
                subjectName,
                title,
                content,
                gId,
                cid,
                groupName,
                ims,
                vs,
                visiblePermission);
    }




    public AppActivityDTO(AppActivityEntry entry){
        this.id=entry.getID().toString();
        this.subjectId=entry.getSubjectId().toString();
        this.userId=entry.getUserId().toString();
        this.subjectName=entry.getSubjectName();
        this.title=entry.getTitle();
        this.content=entry.getContent();
        this.groupId=entry.getGroupId().toString();
        this.communityId=entry.getCommunityId().toString();
        this.groupName=entry.getGroupName();
        List<AttachmentEntry> imageEntries=entry.getImageList();
        for(AttachmentEntry attachmentEntry:imageEntries){
            imageList.add(new Attachement(attachmentEntry));
        }
        List<VideoEntry> videoEntries = entry.getVideoEntries();
        for(VideoEntry videoEntry:videoEntries){
            videoDTOs.add(new VideoDTO(videoEntry));
        }
        this.partInCount=entry.getPartInCount();
        this.visiblePermission = entry.getVisiblePermission();
    }

    public int getManageDelete() {
        return manageDelete;
    }

    public void setManageDelete(int manageDelete) {
        this.manageDelete = manageDelete;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isPartIn() {
        return isPartIn;
    }
    public boolean getIsPartIn() {
        return isPartIn;
    }
    public void setPartIn(boolean partIn) {
        isPartIn = partIn;
    }
    public void setIsPartIn(boolean isPartIn) {
        this.isPartIn = isPartIn;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<GroupOfCommunityDTO> getGroupOfCommunityDTOs() {
        return groupOfCommunityDTOs;
    }

    public void setGroupOfCommunityDTOs(List<GroupOfCommunityDTO> groupOfCommunityDTOs) {
        this.groupOfCommunityDTOs = groupOfCommunityDTOs;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isOwner() {
        return isOwner;
    }
    public boolean getIsOwner() {
        return isOwner;
    }
    public void setOwner(boolean owner) {
        isOwner = owner;
    }
    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public List<VideoDTO> getVideoDTOs() {
        return videoDTOs;
    }

    public void setVideoDTOs(List<VideoDTO> videoDTOs) {
        this.videoDTOs = videoDTOs;
    }

    public int getPartInCount() {
        return partInCount;
    }

    public void setPartInCount(int partInCount) {
        this.partInCount = partInCount;
    }

    public int getVisiblePermission() {
        return visiblePermission;
    }

    public void setVisiblePermission(int visiblePermission) {
        this.visiblePermission = visiblePermission;
    }
}
