package com.pojo.reward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * @author cxy
 * 2015-07-18 15:54:35
 * 
 *  奖惩Entry类
 * collectionName:reward
 *  学期: tt (termType)为根据当前添加时，数据库中的当前学期自动存入，根据设计，不提供用户选择功能。例："2015-1"为2015年第一学期
 * 	年级: gid 年级的ID
 * 	年级: gna 年级的name
 * 	班级：cid 班级的ID
 * 	班级：cna 班级的name
 * 	类型：rt (rewardType)奖惩类型
 * 	等级：rg (rewardGrade)奖惩等级
 * 	学生：sid (studentId)学生的ID
 * 	学生姓名: sna (studentName)学生的姓名
 * 	内容：rc (rewardContent) 奖惩的详细内容
 * 	奖惩日期：rd (rewardDate) 奖惩的执行日期 long
 * 	教务组公示范围[]：prt (publicityRangeTeachers)
 * 	学  生  公 示范围[]：prs (publicityRangeStudents)
 * 	删除标志位：ir 0表示未删除，1表示已删除
 *  学校: scid (shcoolId)
 */
public class RewardEntry extends BaseDBObject {
	
	public RewardEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public RewardEntry(String termType, ObjectId gradeId, ObjectId classId,
			String rewardType, String rewardGrade, String studentId,String studentName, String rewardContent,
			List<ObjectId> publicityRangeTeachers, List<ObjectId> publicityRangeStudents,ObjectId shcoolId,long rewardDate,
			String gradeName,String className) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("tt", termType)
		.append("gid", gradeId)
		.append("cid", classId)
		.append("rt", rewardType)
		.append("rg", rewardGrade)
		.append("sid", studentId)
		.append("sna", studentName)
		.append("rc", rewardContent)
		.append("rd", rewardDate)
		.append("prt", MongoUtils.convert(publicityRangeTeachers))
		.append("prs", MongoUtils.convert(publicityRangeStudents))
		.append("ir", Constant.ZERO)
		.append("scid", shcoolId)
		.append("gna", gradeName)
		.append("cna", className);
		
		setBaseEntry(baseEntry);
		
	}
	
	public String getGradeName() {
		return getSimpleStringValue("gna");
	}
	public void setGradeName(String gradeName) {
		setSimpleValue("gna", gradeName);
	}
	
	public String getClassName() {
		return getSimpleStringValue("cna");
	}
	public void setClassName(String className) {
		setSimpleValue("cna", className);
	}
	
	public String getTermType() {
		return getSimpleStringValue("tt");
	}
	public void setTermType(String termType) {
		setSimpleValue("tt", termType);
	}
	
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoolId(ObjectId schoolId) {
		setSimpleValue("scid", schoolId);
	}
	
	public ObjectId getClassId() {
		return getSimpleObjecIDValue("cid");
	}
	public void setClassId(ObjectId classId) {
		setSimpleValue("cid", classId);
	}
	
	public String getRewardType() {
		return getSimpleStringValue("rt");
	}
	public void setRewardType(String rewardType) {
		setSimpleValue("rt", rewardType);
	}
	
	public String getRewardGrade() {
		return getSimpleStringValue("rg");
	}
	public void setRewardGrade(String rewardGrade) {
		setSimpleValue("rg", rewardGrade);
	}
	
	public String getStudentId() {
		return getSimpleStringValue("sid");
	}
	public void setStudentId(String studentId) {
		setSimpleValue("sid", studentId);
	}
	
	public String getStudentName() {
		return getSimpleStringValue("sna");
	}
	public void setStudentName(String studentName) {
		setSimpleValue("sna", studentName);
	}
	
	public String getRewardContent() {
		return getSimpleStringValue("rc");
	}
	public void setRewardContent(String rewardContent) {
		setSimpleValue("rc", rewardContent);
	}
	
	public long getRewardTime() {
		return getSimpleLongValue("rd"); 
	}
	public void setRewardTime(long rewardTime) {
		setSimpleValue("rd", rewardTime);
	}
	
	public List<ObjectId> getPublicityRangeTeachers() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("prt");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	
	public void setPublicityRangeTeachers(List<ObjectId> publicityRangeTeachers) {
		setSimpleValue("prt", MongoUtils.convert(publicityRangeTeachers));
	}
	
	public List<ObjectId> getPublicityRangeStudents() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("prs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	
	public void setPublicityRangeStudents(List<ObjectId> publicityRangeStudents) {
		setSimpleValue("prs", MongoUtils.convert(publicityRangeStudents));
	}
	//默认未删除
	public int getIsRemove() {
		if(getBaseEntry().containsField("ir"))
		{
		  return getSimpleIntegerValue("ir");
		}
		return Constant.ZERO;
	}
	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}
	
}
