package com.pojo.project;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         课题研究Entry类 
 *         collectionName : project 
 *         发布ID : puid(projectUserId) 
 *         发布人名称 : puna(projectUserName) 
 *         标题名称 : ptna(projectTitleName) 
 *         课题级别 : pl(projectLevel) 
 *         封面图片: pc(projectCover)
 *         发布时间 : pt(publishTime) long
 *         开始时间 : st(startTime)    long
 *         结束时间 : et(endTime)    long
 *         简介 : pp(projectProfile) 
 */
public class ProjectEntry extends BaseDBObject{
	
	public ProjectEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ProjectEntry(ObjectId projectUserId,String projectUserName,String projectTitleName,String projectLevel,
						long startTime,long endTime,String projectProfile,long publishTime,String projectCover) {
			super();

			BasicDBObject baseEntry = new BasicDBObject().append("puid", projectUserId)
									 					 .append("puna", projectUserName)
									 					 .append("ptna", projectTitleName)
									 					 .append("pl", projectLevel)
									 					 .append("st", startTime)
									 					 .append("et", endTime)
									 					 .append("pp", projectProfile)
														 .append("pt", publishTime)
														 .append("pc", projectCover);

			setBaseEntry(baseEntry);
	}
	
	public ObjectId getProjectUserId() {
		return getSimpleObjecIDValue("puid");
	}
	public void setProjectUserId(String projectUserId) {
		setSimpleValue("puid", projectUserId);
	}
	
	public String getProjectUserName() {
		return getSimpleStringValue("puna");
	}
	
	public void setProjectUserName(String projectUserName) {
		setSimpleValue("puna", projectUserName);
	}
	
	public String getProjectTitleName() {
		return getSimpleStringValue("ptna");
	}
	
	public void setProjectTitleName(String projectTitleName) {
		setSimpleValue("ptna", projectTitleName);
	}
	
	public String getProjectLevel() {
		return getSimpleStringValue("pl");
	}
	
	public void setProjectLevel(String projectLevel) {
		setSimpleValue("pl", projectLevel);
	}
	
	public long getStartTime() {
		return getSimpleLongValue("st");
	}
	public void setStartTime(long startTime) {
		setSimpleValue("st", startTime);
	}
	
	public long getEndTime() {
		return getSimpleLongValue("et");
	}
	public void setEndTime(long endTime) {
		setSimpleValue("et", endTime);
	}
	
	public String getProjectProfile() {
		return getSimpleStringValue("pp");
	}
	
	public void setProjectProfile(String projectProfile) {
		setSimpleValue("pp", projectProfile);
	}
	
	public long getPublishTime() {
		return getSimpleLongValue("pt");
	}
	public void setPublishTime(long publishTime) {
		setSimpleValue("pt", publishTime);
	}
	
	public String getProjectCover() {
		return getSimpleStringValue("pc");
	}
	public void setProjectCover(String projectCover) {
		setSimpleValue("pc", projectCover);
	}
}
