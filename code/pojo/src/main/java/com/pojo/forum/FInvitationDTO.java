package com.pojo.forum;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/13.
 */
public class FInvitationDTO {

    private String id;
    private String userId;
    private long count;
    private List<String> userReplyList = new ArrayList<String>();

    public FInvitationDTO(){}

    public FInvitationDTO(FInvitationEntry fInvitationEntry){
        this.id=fInvitationEntry.getID().toString();
        this.userId=fInvitationEntry.getUserId().toString();
        this.count=fInvitationEntry.getCount();
        for(ObjectId id : fInvitationEntry.getUserReplyList()){
            this.userReplyList.add(id.toString());
        }
    }

    public FInvitationEntry exportEntry(){
        FInvitationEntry fInvitationEntry=new FInvitationEntry();
        if(id != null && !id.equals("")){
            fInvitationEntry.setID(new ObjectId(id));
        }
        if(userId != null && !userId.equals("")){
            fInvitationEntry.setUserId(new ObjectId(userId));
        }
        fInvitationEntry.setCount(count);
        List<ObjectId> userReplies = new ArrayList<ObjectId>();
        if(userReplyList.size() > 0){
            for(String id : userReplyList){
                userReplies.add(new ObjectId(id));
            }
        }
        fInvitationEntry.setUserReplyList(userReplies);
        return fInvitationEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<String> getUserReplyList() {
        return userReplyList;
    }

    public void setUserReplyList(List<String> userReplyList) {
        this.userReplyList = userReplyList;
    }
}
