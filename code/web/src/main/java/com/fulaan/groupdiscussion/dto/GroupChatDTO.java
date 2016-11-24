package com.fulaan.groupdiscussion.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pojo.groups.GroupsChatEntry;
import com.sys.utils.CustomDateSerializer;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by wang_xinxin on 2015/3/30.
 */
public class GroupChatDTO {

    private String id;

    private String gid;

    private String roomid;

    private String groupUserid;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date sendDate;

    private String chatContent;

    private String userName;

    private String imageUrl;

    private String sendtime;

    private String chatid;

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getGroupUserid() {
        return groupUserid;
    }

    public void setGroupUserid(String groupUserid) {
        this.groupUserid = groupUserid;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sendDate == null) ? 0 : sendDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupChatDTO other = (GroupChatDTO) obj;
        if (sendDate == null) {
            if (other.sendDate != null)
                return false;
        } else if (!sendDate.equals(other.sendDate))
            return false;
        return true;
    }

    public GroupsChatEntry buildGroupChat() {
        GroupsChatEntry groupsChatEntry = new GroupsChatEntry(this.roomid,this.groupUserid,this.chatContent);
        return groupsChatEntry;
    }

}
