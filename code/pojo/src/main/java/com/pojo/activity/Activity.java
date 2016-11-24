package com.pojo.activity;

import com.pojo.activity.enums.ActStatus;
import com.pojo.activity.enums.ActVisibility;
import com.sys.utils.HtmlUtils;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hao on 2014/10/22.
 */
public class Activity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1974821645483354066L;
	private String id;
    private String name;
    private String organizer;
    private Date createDate;
    private Date eventStartDate;
    private Date eventEndDate;
    private String location;
    private String coverImage;
    private ActStatus status;
    private ActVisibility visible;
    private Integer memberCount;
    private String description;
    private int discuss; //评论数量
    private int image;   //图片数量
    private String organizerName;
    private int organizerRole;
    private String organizerImageUrl;
    private String strEventStartDate;
    private String strEventEndDate;
    private boolean isFriend;
    private int attendCount;

    public Activity(){}
    public Activity(ActivityEntry activityEntry) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.id=activityEntry.getID().toString();
        this.name=activityEntry.getActName();
        if(activityEntry.getOrganizerId()!=null)
        	this.organizer=activityEntry.getOrganizerId().toString();
        this.createDate=new Date(activityEntry.getCreateDate());
        this.eventStartDate=new Date(activityEntry.getActStartDate());
        this.eventEndDate=new Date(activityEntry.getActEndDate());
        this.location=activityEntry.getLocation();
        this.coverImage=activityEntry.getCoverImage();
        this.status= ActStatus.values()[activityEntry.getActStatus()];
        this.visible= ActVisibility.values()[activityEntry.getActVisibility()];
        this.memberCount=activityEntry.getMemberCount();
        this.description=activityEntry.getDescription();
        this.discuss=activityEntry.getDiscussCount();
        this.image=activityEntry.getImageCount();
        String endDate=simpleDateFormat.format(activityEntry.getActEndDate());
        String startDate=simpleDateFormat.format(activityEntry.getActStartDate());
        this.strEventEndDate=endDate;
        this.strEventStartDate=startDate;
        this.attendCount=activityEntry.getAttendCount();
        
        //this.organizerName=activityEntry.getOrganizerId().toString();


    }



    public int getAttendCount() {
        return attendCount;
    }

    public void setAttendCount(int attendCount) {
        this.attendCount = attendCount;
    }
    public boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public String getStrEventEndDate() {
        return strEventEndDate;
    }

    public void setStrEventEndDate(String strEventEndDate) {
        this.strEventEndDate = strEventEndDate;
    }

    public String getStrEventStartDate() {
        return strEventStartDate;
    }

    public void setStrEventStartDate(String strEventStartDate) {
        this.strEventStartDate = strEventStartDate;
    }

    public String getOrganizerImageUrl() {
        return organizerImageUrl;
    }

    public void setOrganizerImageUrl(String organizerImageUrl) {
        this.organizerImageUrl = organizerImageUrl;
    }

    public int getOrganizerRole() {
        return organizerRole;
    }

    public void setOrganizerRole(int organizerRole) {
        this.organizerRole = organizerRole;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public ActStatus getStatus() {
        return status;
    }

    public void setStatus(ActStatus status) {
        this.status = status;
    }

    public ActVisibility getVisible() {
        return visible;
    }

    public void setVisible(ActVisibility visible) {
        this.visible = visible;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
    	description=HtmlUtils.delScriptTag(description);
        this.description = description;
    }

    
    public int getDiscuss() {
		return discuss;
	}

	public void setDiscuss(int discuss) {
		this.discuss = discuss;
	}

	

	
	
	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (coverImage != null ? !coverImage.equals(activity.coverImage) : activity.coverImage != null) return false;
        if (createDate != null ? !createDate.equals(activity.createDate) : activity.createDate != null) return false;
        if (description != null ? !description.equals(activity.description) : activity.description != null)
            return false;
        if (eventEndDate != null ? !eventEndDate.equals(activity.eventEndDate) : activity.eventEndDate != null)
            return false;
        if (eventStartDate != null ? !eventStartDate.equals(activity.eventStartDate) : activity.eventStartDate != null)
            return false;
        if (id != null ? !id.equals(activity.id) : activity.id != null) return false;
        if (location != null ? !location.equals(activity.location) : activity.location != null) return false;
        if (memberCount != null ? !memberCount.equals(activity.memberCount) : activity.memberCount != null)
            return false;
        if (name != null ? !name.equals(activity.name) : activity.name != null) return false;
        if (organizer != null ? !organizer.equals(activity.organizer) : activity.organizer != null) return false;
        if (status != null ? !status.equals(activity.status) : activity.status != null) return false;
        if (visible != null ? !visible.equals(activity.visible) : activity.visible != null) return false;

        return true;
    }



    public ActivityEntry exportActivity() {
         ActivityEntry activityEntry=new ActivityEntry();
        activityEntry.setActEndDate(this.eventEndDate.getTime());
        activityEntry.setActName(this.name);
        activityEntry.setActStartDate(this.eventStartDate.getTime());
        activityEntry.setActStatus(this.getStatus().getState());
        activityEntry.setActVisibility(this.visible.getState());
        activityEntry.setAttendCount(this.attendCount);
        activityEntry.setCoverImage(this.coverImage);
        activityEntry.setCreateDate(this.createDate.getTime());
        activityEntry.setDescription(this.description);
        activityEntry.setDiscussCount(this.discuss);
        activityEntry.setImageCount(this.image);
        activityEntry.setLocation(this.location);
        activityEntry.setMemberCount(this.memberCount);
        activityEntry.setOrganizerId(new ObjectId(this.organizer));
//        activityEntry.setOrganizerImageUrl(this.getOrganizerImageUrl());
//        activityEntry.setOrganizerName(this.organizerName);
//        activityEntry.setOrganizerRole(this.organizerRole);
//        activityEntry.setRegionId();
//        activityEntry.setSchoolId();
//        activityEntry.setAttendIds();
        return activityEntry;
    }
}
