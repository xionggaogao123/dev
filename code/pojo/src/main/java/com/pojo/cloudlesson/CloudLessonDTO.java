package com.pojo.cloudlesson;

import java.io.Serializable;

import com.pojo.video.VideoEntry;
/**
 * 云课程DTO
 * @author fourer
 *
 */
public class CloudLessonDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5791071587097001186L;
	private String id;
	private String name;
	private String imageUrl; //图片地址
	private String path; //视频地址
	private long size;
	private String bucketKey;
	private int VideoSourceType;
	
	
	public CloudLessonDTO(CloudLessonEntry e,VideoEntry ve)
	{
		this.id=e.getID().toString();
		this.name=e.getName().toString();
		this.imageUrl =e.getImageUrl();
		this.path="";//todo :  by autman
		this.size=ve.getLength();
		this.bucketKey=ve.getBucketkey();
		this.VideoSourceType=ve.getVideoSourceType();
	}
	
	public CloudLessonDTO()
	{
		
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
	
	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getBucketKey() {
		return bucketKey;
	}
	public void setBucketKey(String bucketKey) {
		this.bucketKey = bucketKey;
	}
	public int getVideoSourceType() {
		return VideoSourceType;
	}
	public void setVideoSourceType(int videoSourceType) {
		VideoSourceType = videoSourceType;
	}


	@Override
	public String toString() {
		return "CloudClassDTO [id=" + id + ", name=" + name + ", imageUrl="
				+ imageUrl + ", path=" + path + ", size=" + size
				+ ", bucketKey=" + bucketKey + ", VideoSourceType="
				+ VideoSourceType + "]";
	}
	
	
	
	

}
