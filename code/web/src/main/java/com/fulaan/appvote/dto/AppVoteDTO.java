package com.fulaan.appvote.dto;

import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.pojo.appvote.AppVoteEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/6.
 */
public class AppVoteDTO {

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
    private List<GroupOfCommunityDTO> groupExamDetailDTOs = new ArrayList<GroupOfCommunityDTO>();
    private List<VoteResult> voteResultList=new ArrayList<VoteResult>();
    private List<User> voteUsers=new ArrayList<User>();
    private List<String> voteContent = new ArrayList<String>();
    private int voteMaxCount;
    private long voteDeadTime;
    private String deadFormatTime;
    private int voteType;
    private int visiblePermission;
    private int commentCount;
    private int voteCount;
    private int voteDeadFlag;

    //额外添加字段
    private int isVoted;
    private String userName;
    private String deadTime;
    private String avatar;

    private boolean isOwner;
    private String submitTime;


    public AppVoteDTO() {

    }

    public AppVoteDTO(String subjectId, String userId,
                      String subjectName,
                      String title,
                      String content,
                      List<Attachement> attachements,
                      List<String> voteContent,
                      int voteMaxCount,
                      long voteDeadTime,
                      int voteType,
                      int visiblePermission,
                      String groupId,
                      String communityId,
                      String groupName) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.subjectName = subjectName;
        this.title = title;
        this.content = content;
        this.imageList.addAll(attachements);
        this.voteContent.addAll(voteContent);
        this.voteMaxCount = voteMaxCount;
        this.voteDeadTime = voteDeadTime;
        this.voteType = voteType;
        this.visiblePermission = visiblePermission;
        this.groupId=groupId;
        this.communityId=communityId;
        this.groupName=groupName;
    }

    public AppVoteEntry buildEntry() {
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
        return new AppVoteEntry(suId,
                uId,
                subjectName,
                title,
                content,
                gId,
                cid,
                groupName,
                ims,
                voteContent,
                voteMaxCount,
                voteDeadTime,
                voteType,
                visiblePermission);
    }

    public AppVoteDTO(AppVoteEntry appVoteEntry) {
        this.id = appVoteEntry.getID().toString();
        this.communityId = appVoteEntry.getCommunityId().toString();
        this.content = appVoteEntry.getContent();
        this.subjectId = appVoteEntry.getSubjectId().toString();
        this.subjectName = appVoteEntry.getSubjectName();
        this.groupName = appVoteEntry.getGroupName();
        this.title = appVoteEntry.getTitle();
        this.userId = appVoteEntry.getUserId().toString();
        this.groupId = appVoteEntry.getGroupId().toString();
        List<AttachmentEntry> attachmentEntries = appVoteEntry.getImageList();
        for (AttachmentEntry entry : attachmentEntries) {
            imageList.add(new Attachement(entry));
        }
        this.voteContent = appVoteEntry.getVoteContent();
        this.voteMaxCount = appVoteEntry.getVoteMaxCount();
        this.voteDeadTime = appVoteEntry.getVoteDeadTime();
        this.voteType = appVoteEntry.getVoteType();
        this.visiblePermission = appVoteEntry.getVisiblePermission();
        this.commentCount = appVoteEntry.getCommentCount();
        this.deadTime = DateTimeUtils.convert(appVoteEntry.getVoteDeadTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        this.voteCount = appVoteEntry.getVoteCount();
    }



    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public int getVoteDeadFlag() {
        return voteDeadFlag;
    }

    public void setVoteDeadFlag(int voteDeadFlag) {
        this.voteDeadFlag = voteDeadFlag;
    }

    public List<User> getVoteUsers() {
        return voteUsers;
    }

    public void setVoteUsers(List<User> voteUsers) {
        this.voteUsers = voteUsers;
    }

    public String getDeadFormatTime() {
        return deadFormatTime;
    }

    public void setDeadFormatTime(String deadFormatTime) {
        this.deadFormatTime = deadFormatTime;
    }

    public List<GroupOfCommunityDTO> getGroupExamDetailDTOs() {
        return groupExamDetailDTOs;
    }

    public void setGroupExamDetailDTOs(List<GroupOfCommunityDTO> groupExamDetailDTOs) {
        this.groupExamDetailDTOs = groupExamDetailDTOs;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getIsVoted() {
        return isVoted;
    }

    public void setIsVoted(int isVoted) {
        this.isVoted = isVoted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public List<String> getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(List<String> voteContent) {
        this.voteContent = voteContent;
    }

    public int getVoteMaxCount() {
        return voteMaxCount;
    }

    public void setVoteMaxCount(int voteMaxCount) {
        this.voteMaxCount = voteMaxCount;
    }

    public long getVoteDeadTime() {
        return voteDeadTime;
    }

    public void setVoteDeadTime(long voteDeadTime) {
        this.voteDeadTime = voteDeadTime;
    }

    public int getVoteType() {
        return voteType;
    }

    public void setVoteType(int voteType) {
        this.voteType = voteType;
    }

    public int getVisiblePermission() {
        return visiblePermission;
    }

    public void setVisiblePermission(int visiblePermission) {
        this.visiblePermission = visiblePermission;
    }

    public List<VoteResult> getVoteResultList() {
        return voteResultList;
    }

    public void setVoteResultList(List<VoteResult> voteResultList) {
        this.voteResultList = voteResultList;
    }
}
