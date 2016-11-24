package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/15.
 */
public class ReportedDTO {

    private String id;
    private long   time;
    private String userId;
    private String reason;
    private String nickName;

    public ReportedDTO() {
    }

    public ReportedDTO(String id, long time, String userId, String reason) {
        this.id = id;
        this.time = time;
        this.userId = userId;
        this.reason = reason;
    }

    public ReportedDTO(FPostEntry.Reported reported){
        this(reported.getID() != null ? reported.getID().toString() : null, reported.getTime(),
                reported.getUserId() != null ? reported.getUserId().toString() : null, reported.getReason());
    }

    public FPostEntry.Reported exportEntry(){
        if(null==id||id.equals("")){
            id = new ObjectId().toString();
        }

        if(null==userId||userId.equals("")){
            userId = new ObjectId().toString();
        }
        FPostEntry.Reported reported = new FPostEntry.Reported(new ObjectId(id),new ObjectId(userId), time,reason);
        return reported;
    }

    public String getId() {
        if(null==id||id.equals("")){
            return new ObjectId().toString();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        if(null==userId||userId.equals("")){
            return new ObjectId().toString();
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
