package com.pojo.dorm;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
/**
 * 宿舍学生集合，依附于DormEntry
 *  @author caotiecheng 2015/12/8
 * 生成时间 ： ct (createTime)long
 * 性别：sex 
 * 宿舍id：did(dormId)
 * 学号：stn(studentNum)
 * 床位：bd(bed)
 * 学生id：stid(studentid)
 * 学生名字：stnm(studentName)
 * 年级：grd(grade)
 * 班级：cl(dlass)
 * 领取物品：regs(receiveGoods)
 */
public class DormStudentEntry extends BaseDBObject {
	public DormStudentEntry(BasicDBObject baseEntry){
		super(baseEntry);
	};
	public DormStudentEntry(ObjectId schoolId,ObjectId dormId,String dormName,
			ObjectId gradeId,ObjectId classId,String studentNum,int bed,ObjectId studentId,String studentName,
			String sex,String grade,String dlass,String receiveGoods,long createTime){
		super();
		BasicDBObject baseEntry = new BasicDBObject()
		.append("sid",schoolId)
		.append("did",dormId)
		.append("dn",dormName)
		.append("ct",createTime)
		.append("stn",studentNum)
		.append("bd",bed)
		.append("sex",sex)
		.append("gid",gradeId)
		.append("cid",classId)
		.append("stid", studentId)
		.append("stnm",studentName)
		.append("grd",grade)
		.append("cl",dlass)
		.append("regs",receiveGoods);
		setBaseEntry(baseEntry);
	}

	public ObjectId getSchoolId(){
		return getSimpleObjecIDValue("sid");
	}
	public void setSchoolId(ObjectId schoolId){
		setSimpleValue("sid",schoolId);
	}
	
	public ObjectId getDormId(){
		return getSimpleObjecIDValue("did");
	}
	public void setDormId(ObjectId dormId){
		setSimpleValue("did",dormId);
	}
	
	public String getDormName(){
		return getSimpleStringValue("dn");
	}
	public void setDormName(String dormName){
		setSimpleValue("dn",dormName);
	}
	
	public long getCreatTime(){
		return getSimpleLongValue("ct");
	}
	public void setCreatTime(long createTime){
		setSimpleValue("ct", createTime);
	}
	
	public ObjectId getStudentId(){
		return getSimpleObjecIDValue("stid");
	}
	public void setStudentId(ObjectId studentId){
		setSimpleValue("stid",studentId);
	}
	
	public String getStudentNum(){
		return getSimpleStringValue("stn");
	}
	public void setStudentNum(String studentNum){
		setSimpleValue("stn",studentNum);
	}

	public int getBed(){
		return getSimpleIntegerValue("bd");
	}
	public void setBed(int bed){
		setSimpleValue("bd",bed);
	}

	public String getSex(){
		return getSimpleStringValue("sex");
	}
	public void setSex(String sex){
		setSimpleValue("sex",sex);
	}
	
	public ObjectId getGradeId(){
		return getSimpleObjecIDValue("gid");
	}
	public void setGradeId(ObjectId studentId){
		setSimpleValue("gid",studentId);
	}
	
	public ObjectId getClassId(){
		return getSimpleObjecIDValue("cid");
	}
	public void setClassId(ObjectId classId){
		setSimpleValue("cid",classId);
	}
	
	public String getStudentName(){
		return getSimpleStringValue("stnm");
	}
	public void setStudentName(String studentName){
		setSimpleValue("stnm",studentName);
	}

	public String getGrade(){
		return getSimpleStringValue("grd");
	}
	public void setGrade(String grade){
		setSimpleValue("grd",grade);
	}

	public String getDlass(){
		return getSimpleStringValue("cl");
	}
	public void setDlass(String dlass){
		setSimpleValue("cl",dlass);
	}

	public String getReceiveGoods(){
		return getSimpleStringValue("regs");
	}
	public void setReceiveGoods(String receiveGoods){
		setSimpleValue("regs",receiveGoods);
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
