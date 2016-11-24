package com.pojo.lesson;

import java.io.Serializable;

import com.sys.constants.Constant;
/**
 * 课程DTO
 * @author fourer
 *
 */
public class LessonDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7756008854133779372L;

	private String id;
	private String name;
	private String imgUrl;
	
	private int videoCount;
	private int documentCount;
	private int exerciseCount;
	private boolean isFromCloud;
	private boolean isdelete;
	
	
	public LessonDTO(LessonEntry e) {
		super();
		this.id=e.getID().toString();
		this.name=e.getName();
		this.imgUrl=e.getImgUrl();
		if(e.getBaseEntry().containsField("vc"))
		{
		   this.videoCount=e.getVideoCount();
		}
		if(e.getBaseEntry().containsField("dc"))
		{
		  this.documentCount=e.getLessonWareCount();
		}
		if(e.getBaseEntry().containsField("ec"))
		{
		  this.exerciseCount=e.getExerciseCount();
		}
		if(e.getBaseEntry().containsField("isc"))
		{
		  this.isFromCloud=(Constant.ONE==e.getIsFromCloud());
		}
	}
	
	//todo
	public LessonDTO() {
		
	}
	
	
	//IOs 更新比较慢，所以增加ImageUrl字段
	public String getImageUrl() {
		return imgUrl;
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getVideoCount() {
		return videoCount;
	}
	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}
	public int getDocumentCount() {
		return documentCount;
	}
	public void setDocumentCount(int documentCount) {
		this.documentCount = documentCount;
	}
	public int getExerciseCount() {
		return exerciseCount;
	}
	public void setExerciseCount(int exerciseCount) {
		this.exerciseCount = exerciseCount;
	}

	public boolean getIsFromCloud() {
		return isFromCloud;
	}

	public void setIsFromCloud(boolean isFromCloud) {
		this.isFromCloud = isFromCloud;
	}

	public boolean isIsdelete() {
		return isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}
}
