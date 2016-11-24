package com.pojo.dangjian;

import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/6/14.
 */
public class PartyUserDTO {

    private String id;
    private String userId;
    private String userName;
    private String schoolId;
    private int isPartyMember;
    private int isCenterMember;
    private int isPartySecretary;

    public PartyUserDTO(){}

    public PartyUserDTO(String id, String userId, String userName, String schoolId, int isPartyMember, int isCenterMember, int isPartySecretary) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.schoolId = schoolId;
        this.isPartyMember = isPartyMember;
        this.isCenterMember = isCenterMember;
        this.isPartySecretary = isPartySecretary;
    }

    public PartyUserDTO(String userId) {
        this.userId = userId;
        this.isPartyMember = 0;
        this.isCenterMember = 0;
        this.isPartySecretary = 0;
    }

    public PartyUserDTO(PartyUser partyUser){
        this.id = partyUser.getID().toString();
        this.userId = partyUser.getUserId().toString();
        this.schoolId = partyUser.getSchoolId().toString();
        this.isPartyMember = partyUser.getIsPartyMember();
        this.isCenterMember = partyUser.getIsCenterMember();
        this.isPartySecretary = partyUser.getIsPartySecretary();
    }

    public PartyUser exportEntry(){
        PartyUser partyUser = new PartyUser(new ObjectId(userId), new ObjectId(schoolId), isPartyMember, isCenterMember, isPartySecretary);
        if(id != null){
            partyUser.setID(new ObjectId(id));
        }
        return partyUser;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getIsPartyMember() {
        return isPartyMember;
    }

    public void setIsPartyMember(int isPartyMember) {
        this.isPartyMember = isPartyMember;
    }

    public int getIsCenterMember() {
        return isCenterMember;
    }

    public void setIsCenterMember(int isCenterMember) {
        this.isCenterMember = isCenterMember;
    }

    public int getIsPartySecretary() {
        return isPartySecretary;
    }

    public void setIsPartySecretary(int isPartySecretary) {
        this.isPartySecretary = isPartySecretary;
    }

    //
}
