package com.pojo.registration;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 
 * @author cxy
 * 2015-9-7 10:36:52
 * 学籍信息Entry(用于学籍管理)
 * collectionName : registration 
 * 学生ID : uid(userId)
 * 学生姓名： una(userName)
 * 学号：snu(studentNumber)
 * 年级ID : gid(gradeId)
 * 年级名称 : gna(gradeName)
 * 班级ID : cid(classId)
 * 班级名称 : cna(className)
 * 变动日期：cdt(changeDate)
 * 变动说明：cps(changePostscript)
 * 
 */
public class RegistrationEntry extends BaseDBObject{
	public RegistrationEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public RegistrationEntry(ObjectId userId,String userName,String studentNumber,ObjectId gradeId,String gradeName,ObjectId classId,String className,
											long changeDate,String chagePostscript){
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("uid", userId)
													 .append("una", userName)
													 .append("snu", studentNumber)
													 .append("gid", gradeId)
													 .append("gna", gradeName)
													 .append("cid", classId)
													 .append("cna", className)
													 .append("cdt", changeDate)
													 .append("cps", chagePostscript);
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("uid");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("uid", userId);
	}
	
	public String getUserName() {
		return getSimpleStringValue("una");
	}
	public void setUserName(String userName) {
		setSimpleValue("una", userName);
	}
	
	public String getStudentNumber() {
		return getSimpleStringValue("snu");
	}
	public void setStudentNumber(String studentNumber) {
		setSimpleValue("snu", studentNumber);
	}
	
	public ObjectId getClassId() {
		return getSimpleObjecIDValue("cid");
	}
	public void setClassId(ObjectId classId) {
		setSimpleValue("cid", classId);
	}
	
	public String getClassName() {
		return getSimpleStringValue("cna");
	}
	public void setClassName(String className) {
		setSimpleValue("cna", className);
	}
	
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
	}
	
	public String getGradeName() {
		return getSimpleStringValue("gna");
	}
	public void setGradeName(String gradeName) {
		setSimpleValue("gna", gradeName);
	}
	
	public long getChangeDate() {
		return getSimpleLongValue("cdt");
	}
	public void setChangeDate(String changeDate) {
		setSimpleValue("cdt", changeDate);
	}
	
	public String getChagePostscript() {
		return getSimpleStringValue("cps");
	}
	public void setChagePostscript(String chagePostscript) {
		setSimpleValue("cps", chagePostscript);
	}
}
