package com.fulaan.playmate.dto;

import com.fulaan.util.DateUtils;
import com.pojo.playmate.FActivityEntry;
import com.pojo.user.UserTag;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by moslpc on 2016/12/2.
 */
public class ActivityDTO {

    private String acid;
    private String userId;
    private String title;
    private String description;
    private String createTime;
    private String activityTime;
    private int signCount;
    private int activityCode;
    private String distance;

    private User user;
    private List<Map<String,Object>> signSheets;

    public ActivityDTO(FActivityEntry entry){
        this.acid = entry.getID().toString();
        this.userId = entry.getUserId().toString();
        this.title = entry.getTitle();
        this.description = entry.getDescription();
        this.createTime = DateUtils.timeStampToStr(entry.getCreateTime() / 1000);
        this.activityTime = DateUtils.timeStampToStr(entry.getActivityTime() / 1000);
        this.activityCode = entry.getACode();
    }

    public String getAcid() {
        return acid;
    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public int getSignCount() {
        return signCount;
    }

    public void setSignCount(int signCount) {
        this.signCount = signCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public User getUser() {
        return user;
    }

    public int getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(int activityCode) {
        this.activityCode = activityCode;
    }

    public void setUser(ObjectId userId, String nickName, String userName, String avatar, List<UserTag> tags) {
        User user = new User();
        user.userId = userId.toString();
        user.nickName = nickName;
        user.userName = userName;
        user.avatar = avatar;
        user.tags = tags;
        this.user = user;
    }

    class User{
        public String userId;
        public String nickName;
        public String userName;
        public String avatar;
        public List<UserTag> tags;
    }

    public List<Map<String, Object>> getSignSheets() {
        return signSheets;
    }

    public void setSignSheets(List<Map<String, Object>> signSheets) {
        this.signSheets = signSheets;
    }
}
