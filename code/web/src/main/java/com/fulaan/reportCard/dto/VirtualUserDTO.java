package com.fulaan.reportCard.dto;

import com.pojo.reportCard.VirtualUserEntry;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/17.
 */
public class VirtualUserDTO {

    private String communityId;
    private String userNumber;
    private String userId;
    private String userName;
    private String id;

    public VirtualUserDTO(){

    }

    public VirtualUserEntry buildEntry(){
        VirtualUserEntry
                entry=new VirtualUserEntry(new ObjectId(communityId),userNumber,new ObjectId(userId),userName);
        return entry;
    }

    public VirtualUserDTO(VirtualUserEntry entry){
        this.communityId=entry.getCommunityId().toString();
        //this.userNumber=entry.getUserNumber().toString();
        this.userId=entry.getUserId().toString();
        this.userName=entry.getUserName();
        this.id=entry.getID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
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
}
