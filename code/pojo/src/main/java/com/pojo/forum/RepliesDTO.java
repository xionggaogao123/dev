package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/6/20.
 */
public class RepliesDTO {
    private String id;
    private String nickName;
    private String content;
    private long   time;

    //显示时间
    private String timeText;
    private String imageStr;

    //保存回复人Id
    private String personId;
    private String userId;
    private String rpid;


    public RepliesDTO(){}
    public RepliesDTO(String id, String nickName, String content,long time,String imageStr,String personId,String userId) {
        this.id = id;
        this.nickName = nickName;
        this.content = content;
        this.time = time;
        this.imageStr = imageStr;
        this.personId = personId;
        this.userId = userId;
    }

    public RepliesDTO(FReplyEntry.Replies replies){
        this(replies.getId()!=null?replies.getId().toString():null, replies.getNickName(), replies.getContent(),replies.getTime(),replies.getImageStr(),
                replies.getPersonId()!=null?replies.getPersonId().toString():null,replies.getUserId()!=null?replies.getUserId().toString():null);
    }
    public FReplyEntry.Replies exportEntry(){
        if(null==id||id.equals("")){
            id = new ObjectId().toString();
        }
        if(null==personId||personId.equals("")){
            personId = new ObjectId().toString();
        }
        if(null==userId||userId.equals("")){
            userId = new ObjectId().toString();
        }
        FReplyEntry.Replies replies = new FReplyEntry.Replies(new ObjectId(id), nickName, content,time,imageStr,
                new ObjectId(personId),new ObjectId(userId));
        return replies;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public String getRpid() {
        return rpid;
    }

    public void setRpid(String rpid) {
        this.rpid = rpid;
    }

    public String getPersonId() {
        if(null==personId||personId.equals("")){
            return new ObjectId().toString();
        }
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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
}
