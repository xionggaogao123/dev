package com.pojo.forum;

import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/4.
 */
public class FMissionDTO {

    private String fMissionId;
    private String personId;
    private String time;
    private int    signIn;
    private int    post;
    private int    welfare;
    private int    count;

    public FMissionDTO(){}

    public FMissionDTO(FMissionEntry fMissionEntry){
        this.fMissionId=fMissionEntry.getID().toString();
        this.personId=fMissionEntry.getPersonId().toString();
        this.time=fMissionEntry.getTime();
        this.signIn=fMissionEntry.getSignIn();
        this.post=fMissionEntry.getPost();
        this.welfare=fMissionEntry.getWelfare();
        this.count=fMissionEntry.getCount();
    }

    public FMissionEntry exportEntry(){
        FMissionEntry fMissionEntry=new FMissionEntry();
        if(fMissionId != null && !fMissionId.equals("")){
            fMissionEntry.setID(new ObjectId(fMissionId));
        }
        if(!personId.equals("")){
            fMissionEntry.setPersonId(new ObjectId(personId));
        }
        fMissionEntry.setTime(time);
        fMissionEntry.setPost(post);
        fMissionEntry.setSignIn(signIn);
        fMissionEntry.setWelfare(welfare);
        fMissionEntry.setCount(count);
        return fMissionEntry;
    }

    public String getfMissionId() {
        return fMissionId;
    }

    public void setfMissionId(String fMissionId) {
        this.fMissionId = fMissionId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public int getSignIn() {
        return signIn;
    }

    public void setSignIn(int signIn) {
        this.signIn = signIn;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getWelfare() {
        return welfare;
    }

    public void setWelfare(int welfare) {
        this.welfare = welfare;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
