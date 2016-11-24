package com.pojo.activity;

/**
 * Created by Hao on 2014/10/22.
 */
public class ActAttend {
    private String userId;
    private String activityId;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActAttend actAttend = (ActAttend) o;

        if (activityId != null ? !activityId.equals(actAttend.activityId) : actAttend.activityId != null) return false;
        if (userId != null ? !userId.equals(actAttend.userId) : actAttend.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (activityId != null ? activityId.hashCode() : 0);
        return result;
    }
}
