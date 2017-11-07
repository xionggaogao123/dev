package com.fulaan.appvote.dto;

import com.fulaan.pojo.Attachement;
import com.pojo.appvote.AppVoteEntry;
import com.pojo.fcommunity.AttachmentEntry;
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
    private List<Attachement> imageList = new ArrayList<Attachement>();
    private List<String> voteContent = new ArrayList<String>();
    private int voteMaxCount;
    private long voteDeadTime;
    private int voteType;
    private int visiblePermission;

    public AppVoteDTO() {

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
}
