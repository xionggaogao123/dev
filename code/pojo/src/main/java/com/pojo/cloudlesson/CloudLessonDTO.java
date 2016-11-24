package com.pojo.cloudlesson;

import com.pojo.school.SubjectType;
import com.pojo.video.VideoEntry;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private String videoId;
	private String name;
	private String type;
	private String typeName;
	private String createrName;
	private String createrAvt;
	private int schoolType;
	private String schoolTypeName;
	private String schoolName;
	private int gradeType;
	private String gradeTypeName;
	private int subjectType;
	private String subjectName;
	private String imageUrl; //图片地址
	private String path; //视频地址
	private String from;
	private long size;
	private String bucketKey;
	private int VideoSourceType;
	private String uploadTime;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public CloudLessonDTO(CloudLessonEntry e, VideoEntry ve)
	{
		this.id=e.getID().toString();
		this.videoId=ve.getID().toString();
		this.name=e.getName().toString();
		this.subjectType=e.getSubject();
		this.subjectName = SubjectType.getSubjectType(e.getSubject()).getName();
		String url=e.getImageUrl();
		if(url.contains("/upload/")){
			this.imageUrl ="http://www.k6kt.com"+e.getImageUrl();
		}else{
			this.imageUrl = e.getImageUrl();
		}
		this.from=e.getResourceFrom()==null?"管理员上传":e.getResourceFrom();
		this.path="";//todo :  by autman
		this.size=ve.getLength();
		this.bucketKey=ve.getBucketkey();
		this.VideoSourceType=ve.getVideoSourceType();

		this.uploadTime=sdf.format(new Date(ve.getUpdateDate()));
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

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
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

	public int getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getCreaterAvt() {
		return createrAvt;
	}

	public void setCreaterAvt(String createrAvt) {
		this.createrAvt = createrAvt;
	}

	public String getSchoolTypeName() {
		return schoolTypeName;
	}

	public void setSchoolTypeName(String schoolTypeName) {
		this.schoolTypeName = schoolTypeName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getGradeType() {
		return gradeType;
	}

	public void setGradeType(int gradeType) {
		this.gradeType = gradeType;
	}

	public String getGradeTypeName() {
		return gradeTypeName;
	}

	public void setGradeTypeName(String gradeTypeName) {
		this.gradeTypeName = gradeTypeName;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(int schoolType) {
		this.schoolType = schoolType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "CloudClassDTO [id=" + id + ", name=" + name + ", imageUrl="
				+ imageUrl + ", path=" + path + ", size=" + size
				+ ", bucketKey=" + bucketKey + ", VideoSourceType="
				+ VideoSourceType + "]";
	}





}
