package com.pojo.registration;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
/**
 * @author cxy
 * 2015-9-6 19:36:55
 * 学生基础信息Entry(用于学籍管理)
 *  collectionName : userbaseinfo 
 *  学生ID : uid(userId) 
 *  学生姓名: una(userName)
 *  学号 : snu(studentNumber)
 *  年级ID : gid(gradeId)
 *  年级名称 : gna(gradeName)
 *  班级ID : cid(classId)
 *  班级名称 : cna(className)
 *  性别 : sex(sex)
 *  出生日期 : bd(birthday)
 *  民族 : sra(studentRace) 
 *  健康状态 : she(studentHealth)
 *  现在住址 : pad(presentAddress)
 *  户籍地址 : rad(residenceAddress)
 *  联系电话 : cona(contactNumber)
 *  电子邮箱 : ead(emailAddress)
 *
 */
public class UserBaseInfoEntry extends BaseDBObject{
	public UserBaseInfoEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public UserBaseInfoEntry(ObjectId userId,String userName,String studentNumber,ObjectId gradeId,String gradeName,ObjectId classId,String className,
								String sex,long birthday,String studentRace,String studentHealth,String presentAddress,String residenceAddress,
								String contactNumber,String emailAddress) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("uid", userId)
													 .append("una", userName)
													 .append("snu", studentNumber)
													 .append("gid", gradeId)
													 .append("gna", gradeName)
													 .append("cid", classId)
													 .append("cna", className)
													 .append("sex", sex)
													 .append("bd", birthday)
													 .append("sra", studentRace)
													 .append("she", studentHealth)
													 .append("pad", presentAddress)
													 .append("rad", residenceAddress)
													 .append("cona", contactNumber)
													 .append("ead", emailAddress);
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
	
	public String getSex() {
		return getSimpleStringValue("sex");
	}
	public void setSex(String sex) {
		setSimpleValue("sex", sex);
	}
	
	public long getBirthday() {
		return getSimpleLongValue("bd");
	}
	public void setBirthday(String birthday) {
		setSimpleValue("bd", birthday);
	}
	
	public String getStudentRace() {
		return getSimpleStringValue("sra");
	}
	public void setStudentRace(String studentRace) {
		setSimpleValue("sra", studentRace);
	}
	
	public String getStudentHealth() {
		return getSimpleStringValue("she");
	}
	public void setStudentHealth(String studentHealth) {
		setSimpleValue("she", studentHealth);
	}
	
	public String getPresentAddress() {
		return getSimpleStringValue("pad");
	}
	public void setPresentAddress(String presentAddress) {
		setSimpleValue("pad", presentAddress);
	}
	
	public String getResidenceAddress() {
		return getSimpleStringValue("rad");
	}
	public void setResidenceAddress(String residenceAddress) {
		setSimpleValue("rad", residenceAddress);
	}
	
	public String getContactNumber() {
		return getSimpleStringValue("cona");
	}
	public void setContactNumber(String contactNumber) {
		setSimpleValue("cona", contactNumber);
	}
	
	public String getEmailAddress() {
		return getSimpleStringValue("ead");
	}
	public void setEmailAddress(String emailAddress) {
		setSimpleValue("ead", emailAddress);
	}
}
