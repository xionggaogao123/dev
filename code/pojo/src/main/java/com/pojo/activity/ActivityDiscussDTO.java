package com.pojo.activity;

import com.pojo.user.UserDetailInfoDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 活动评论表
 * @author Hao
 *
 */
public class ActivityDiscussDTO {

	private String id;
	private String userId;
	private String content;
	private String image;
	private Date time;
	private String repId;
    private String userImage;
    private String userName;
    private List<ActivityDiscussDTO> subDiscussList =new ArrayList<ActivityDiscussDTO>(); //下面的子回复

    public ActivityDiscussDTO(){}
    public ActivityDiscussDTO(ActivityDiscuss activityDiscuss) {
        if(activityDiscuss.getId()!=null)
            this.id= activityDiscuss.getId().toString();
        this.userId= activityDiscuss.getUserId()==null ?null : activityDiscuss.getUserId().toString();
        this.content= activityDiscuss.getContent();
        //目前每个评论只允许有一张图片
        this.image= activityDiscuss.getImageList().size()>0? activityDiscuss.getImageList().get(0):"";
        this.time=new Date(activityDiscuss.getDate());
        this.repId=activityDiscuss.getRepId()==null?"": activityDiscuss.getRepId().toString();
    }

    public ActivityDiscussDTO(UserDetailInfoDTO info, ActivityDiscussDTO dis) {
        super();
        this.id=dis.getId();
        this.userId=dis.getUserId();
        this.userName=info.getNickName();
        this.content=dis.getContent();
        this.image=dis.getImage();
        this.time=dis.getTime();
        this.userImage=info.getImgUrl();
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String getRepId() {
		return repId;
	}
	public void setRepId(String repId) {
		this.repId = repId;
	}
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ActivityDiscussDTO> getSubDiscussList() {
        return subDiscussList;
    }

    public void setSubDiscussList(List<ActivityDiscussDTO> subDiscussList) {
        this.subDiscussList = subDiscussList;
    }
    public ActivityDiscuss exportEntry() {
        ActivityDiscuss activityDiscuss =new ActivityDiscuss();
        List<String> imgList=new ArrayList<String>();
        imgList.add(this.image);
        activityDiscuss.setContent(this.content);
        activityDiscuss.setDate(this.time.getTime());
        activityDiscuss.setImageList(imgList);
        activityDiscuss.getImageList().add(this.image);
        if(repId==null || "".equals(repId.trim())){
            activityDiscuss.setRepId(null);
        }else{
            activityDiscuss.setRepId(new ObjectId(this.repId));
        }
        activityDiscuss.setUserId(new ObjectId(this.userId));
        return activityDiscuss;
    }

    public void addSubDiscuss(ActivityDiscussDTO activityDiscussDTO) {
        this.subDiscussList.add(activityDiscussDTO);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
