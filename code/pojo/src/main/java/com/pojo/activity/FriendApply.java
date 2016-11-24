package com.pojo.activity;

import org.bson.types.ObjectId;

import java.util.Date;

public class FriendApply {
    private String id;

    private String userid;

    private String userName;

    private long userRole;

    private String image;

    private String schoolName;

    private String cityName;

    private String mainClassName;

    private String respondent;

    private Date applydate;

    private Date responddate;

    private Integer accepted;

    private String content;

    public FriendApply(){}
    public FriendApply(FriendApplyEntry friendApplyEntry) {
        this.id=friendApplyEntry.getID().toString();
        this.userid=friendApplyEntry.getUserId().toString();
        this.content=friendApplyEntry.getContent();
        this.respondent=friendApplyEntry.getRespondent().toString();
        this.applydate=new Date(friendApplyEntry.getApplyDate());
        try{
        	this.responddate=new Date(friendApplyEntry.getRespondDate());
        }
        catch(Exception e)
        {
        	
        }
        this.accepted=friendApplyEntry.getAccepted();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserRole() {
        return userRole;
    }

    public void setUserRole(long userRole) {
        this.userRole = userRole;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRespondent() {
        return respondent;
    }

    public void setRespondent(String respondent) {
        this.respondent = respondent;
    }

    public Date getApplydate() {
        return applydate;
    }

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

    public Date getResponddate() {
        return responddate;
    }

    public void setResponddate(Date responddate) {
        this.responddate = responddate;
    }

    public Integer getAccepted() {
        return accepted;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public FriendApplyEntry exportEntry() {
        FriendApplyEntry friendApplyEntry=new FriendApplyEntry();

        friendApplyEntry.setApplyDate(this.getApplydate().getTime());
        friendApplyEntry.setContent(this.getContent());
        friendApplyEntry.setRespond(new ObjectId(this.getRespondent()));
        if(this.getResponddate()!=null)
        	friendApplyEntry.setRespondDate(this.getResponddate().getTime());
        friendApplyEntry.setUserId(new ObjectId(this.getUserid()));
        if(this.getId()!=null)
        	friendApplyEntry.setID(new ObjectId(this.getId()));

        return friendApplyEntry;
    }
}