package com.fulaan.groupdiscussion.dto;

import com.pojo.groups.GroupsUser;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by wang_xinxin on 2015/3/31.
 */
public class GroupUserDTO {

    private String userid;

    private String userName;

    private String maxImageURL;

    private boolean isgroupmain;

    private String maingroup;

    //TODO:传入参数
    private String groupid;

    private boolean isread;

    private Date updatetime;

    private String roomid;

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMaxImageURL() {
        return maxImageURL;
    }

    public void setMaxImageURL(String maxImageURL) {
        this.maxImageURL = maxImageURL;
    }

    public boolean isIsgroupmain() {
        return isgroupmain;
    }

    public void setIsgroupmain(boolean isgroupmain) {
        this.isgroupmain = isgroupmain;
    }

    public String getMaingroup() {
        return maingroup;
    }

    public void setMaingroup(String maingroup) {
        this.maingroup = maingroup;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public boolean isIsread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((maxImageURL == null) ? 0 : maxImageURL.hashCode());
        result = prime * result
                + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((userid == null) ? 0 : userid.hashCode());
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
        GroupUserDTO other = (GroupUserDTO) obj;
        if (maxImageURL == null) {
            if (other.maxImageURL != null)
                return false;
        } else if (!maxImageURL.equals(other.maxImageURL))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (userid == null) {
            if (other.userid != null)
                return false;
        } else if (!userid.equals(other.userid))
            return false;
        return true;
    }

    public GroupsUser buildGroupsUser() {
        GroupsUser groupuser = new GroupsUser(
                this.getUserid(),
                1,
                (long)this.updatetime.getTime()/1000
        );
        return groupuser;
    }
}
