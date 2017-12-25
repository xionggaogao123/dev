package com.fulaan.fgroup.dto;

import cn.jiguang.commom.utils.StringUtils;
import com.pojo.groupchatrecord.GroupChatRecordEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/25.
 */
public class GroupChatRecordDTO {

    private String id;
    private String groupId;
    private String userId;
    private String userName;
    private int type;
    private String content;
    private String fileUrl;
    private String imageUrl;
    private String timeStr;
    private String submitDay;

    public GroupChatRecordDTO(){

    }

    public GroupChatRecordEntry buildEntry(){
        ObjectId gId = StringUtils.isNotEmpty(groupId)?new ObjectId(groupId):null;
        ObjectId uId = StringUtils.isNotEmpty(userId)?new ObjectId(userId):null;
        return new GroupChatRecordEntry(gId,uId,type,content,fileUrl,imageUrl);
    }

    public GroupChatRecordDTO(GroupChatRecordEntry entry){
        this.id=entry.getID().toString();
        this.groupId=entry.getGroupId().toString();
        this.userId=entry.getUserId().toString();
        this.type=entry.getType();
        this.content=entry.getContent();
        this.fileUrl=entry.getFileUrl();
        this.imageUrl=entry.getImageUrl();
        this.submitDay=entry.getSubmitDay();
        this.timeStr= DateTimeUtils.convert(entry.getSubmitTime(),DateTimeUtils.DATE_HH_MM_SS);
    }

    public String getSubmitDay() {
        return submitDay;
    }

    public void setSubmitDay(String submitDay) {
        this.submitDay = submitDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}
