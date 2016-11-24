package com.pojo.registration;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * @author cxy
 * 2015-9-12 14:52:02
 * 
 *  学习简历Entry类
 * collectionName : learningresume
 *  学生ID: uid (userId)
 *  起始日期: sd(startDate)
 *  截至日期: ed(endDate)
 *  入学方式: et(entranceType)
 *  就读方式: st(studyType)
 *  学习单位: su(studyUnit)
 *  备注          : ps(postScript)
 * 	删除标志位：ir 0表示未删除，1表示已删除
 */
public class LearningResumeEntry extends BaseDBObject{
	
	public LearningResumeEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public LearningResumeEntry(ObjectId userId,long startDate,long endDate,String entranceType,String studyType,String syudyUnit,String postScript) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("uid", userId)
		.append("sd", startDate)
		.append("ed", endDate)
		.append("et", entranceType)
		.append("st", studyType)
		.append("su", syudyUnit)
		.append("ps", postScript)
		.append("ir", Constant.ZERO);
		
		setBaseEntry(baseEntry);
		
	}
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("uid");
	}
	public void setSchoolId(ObjectId userId) {
		setSimpleValue("uid", userId);
	}
	
	public long getStartDate() {
		return getSimpleLongValue("sd"); 
	}
	public void setStartDate(long startDate) {
		setSimpleValue("sd", startDate);
	}
	
	public long getEndDate() {
		return getSimpleLongValue("ed"); 
	}
	public void setEndDate(long endDate) {
		setSimpleValue("ed", endDate);
	}
	
	public String getEntranceType() {
		return getSimpleStringValue("et");
	}
	public void setEntranceType(String entranceType) {
		setSimpleValue("et", entranceType);
	}
	
	public String getStudyType() {
		return getSimpleStringValue("st");
	}
	public void setStudyType(String studyType) {
		setSimpleValue("st", studyType);
	}
	
	public String getSyudyUnit() {
		return getSimpleStringValue("su");
	}
	public void setSyudyUnit(String syudyUnit) {
		setSimpleValue("su", syudyUnit);
	}
	
	public String getPostScript() {
		return getSimpleStringValue("ps");
	}
	public void setPostScript(String postScript) {
		setSimpleValue("ps", postScript);
	}
}
