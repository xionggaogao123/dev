package com.pojo.activity;


import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActTrackType;

import java.util.Date;

/**
 * Created by Hao on 2014/10/24.
 */
public class ActTrack {
    private String id;
    private String relateId;
    private ActTrackType type;
    private String userId;
    private Date createTime;//动态发生时间
    private ActTrackDevice fromDevice;//来自pc  还是移动端
    //非数据库属性
    private Activity activity;
    private String userName;
    private String userImgUrl;
    private String relateUserName;
    private String timeMsg;//几小时前  几天前等

    public ActTrack(){}
    public ActTrack(ActTrackEntry actTrackEntry) {
        this.id=actTrackEntry.getID().toString();
        this.relateId=actTrackEntry.getRelatedId().toString();
        this.type= ActTrackType.values()[actTrackEntry.getActTrackType()];
        this.userId=actTrackEntry.getUserId().toString();
        this.createTime=new Date(actTrackEntry.getCreateTime());
        this.fromDevice= ActTrackDevice.values()[actTrackEntry.getActTrackDevice()];
        
//        this.activity=actTrackEntry.getActivity();
//        this.userName=actTrackEntry.getUserName();
//        this.userImgUrl=actTrackEntry.getUserImgUrl();
//        this.relateUserName=actTrackEntry.getRelateUserName();
//        this.timeMsg=actTrackEntry.getTimeMsg();
    }


    public String getTimeMsg() {
        return timeMsg;
    }

    public void setTimeMsg(String timeMsg) {
        this.timeMsg = timeMsg;
    }

    public ActTrackDevice getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(ActTrackDevice fromDevice) {
        this.fromDevice = fromDevice;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getRelateUserName() {
        return relateUserName;
    }

    public void setRelateUserName(String relateUserName) {
        this.relateUserName = relateUserName;
    }

    public Activity getActivity() {
        return activity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public ActTrackType getType() {
        return type;
    }

    public void setType(ActTrackType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActTrack actTrack = (ActTrack) o;

        if (id != null ? !id.equals(actTrack.id) : actTrack.id != null) return false;
        if (relateId != null ? !relateId.equals(actTrack.relateId) : actTrack.relateId != null) return false;
        if (type != actTrack.type) return false;
        if (userId != null ? !userId.equals(actTrack.userId) : actTrack.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (relateId != null ? relateId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
