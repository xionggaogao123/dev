package com.pojo.funcitonclassroom;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/** 
 * @author chengwei@ycode.cn
 * @version 2015年12月21日 上午11:08:17 
 * 类说明 
 * 
 * 教室预约Entry
 * 预约id: _id
 * 学校id: sid(schoolId)
 * 教室id: cid(classroomId)
 * 使用者id: uid(userId)
 * 教室名:  cn(classroomName)
 * 开始时间: st(startT
 * 结束时间: et(endTime)ime)
 * 预约时间: at(appointmentTime)
 * 使用者:  user
 * 使用说明: rs(reasons) 
 * 是否删除: ir(isRemove 0:位未删除,1为删除)
 * 学期:   term
 * 
 * 
 */
public class ClassRoomAppointmentEntry extends BaseDBObject{
	//实体类
	public ClassRoomAppointmentEntry(){
		super();
	}
	
	public ClassRoomAppointmentEntry(BasicDBObject baseEntry){
		super(baseEntry);
	}
	
	public ClassRoomAppointmentEntry(ObjectId schoolId, ObjectId classroomId, String classRoomName, Long startTime, Long endTime,
			String user, String reasons, ObjectId userId, String term){
		super();
		
		BasicDBObject baseEntry = new BasicDBObject()
		.append("sid", schoolId)
		.append("cid", classroomId)
		.append("cn", classRoomName)
		.append("st", startTime)
		.append("et", endTime)
		.append("user", user)
		.append("rs", reasons)
		.append("ir", Constant.ZERO)
		.append("uid", userId)
		.append("term", term);
		
		setBaseEntry(baseEntry);
	}
	
	//get/set方法
		
	public ObjectId getSchoolId(){
		return getSimpleObjecIDValue("sid");
	}
	
	public void setSchoolId(ObjectId schoolId){
		setSimpleValue("sid", schoolId);
	}
	
	public ObjectId getUserId(){
		return getSimpleObjecIDValue("uid");
	}
	
	public void setUserId(ObjectId userId){
		setSimpleValue("uid", userId);
	}
	
	public ObjectId getClassRoomId(){
		return getSimpleObjecIDValue("cid");
	}
	
	public void setClassRoomId(ObjectId classRoomId){
		setSimpleValue("cid", classRoomId);
	}
	
	public String getClassRoomName(){
		return getSimpleStringValue("cn");
	}
	
	public void setClassRoomName(String classRoomName){
		setSimpleValue("cn", classRoomName);
	}
	
	public Long getStartTime(){
		return getSimpleLongValue("st");
	}
	
	public void setStartTime(Long startTime){
		setSimpleValue("st", startTime);
	}
	
	public Long getEndTime(){
		return getSimpleLongValue("et");
	}
	
	public void setEndTime(Long endTime){
		setSimpleValue("et", endTime);
	}
	
	public String getUser(){
		return getSimpleStringValue("user");
	}
	
	public void setUser(String user){
		setSimpleValue("user", user);
	}
	
	public String getReasons(){
		return getSimpleStringValue("rs");
	}
	
	public void setReasons(String reasons){
		setSimpleValue("rs", reasons);
	}
	
//	public Long getAppointmentTime(){
//		return getSimpleLongValue("at");
//	}
//	
//	public void setAppointmentTime(){
//		setSimpleValue("at", System.currentTimeMillis());
//	}
	
	//默认为未删除
	public Integer getIsRemove(){
		return getSimpleIntegerValue("ir");
	}
	
	public void setIsRemove(int isRemove){
		setSimpleValue("ir", isRemove);
	}
	
	public String getTerm(){
		return getSimpleStringValue("term");
	}
	
	public void setTerm(String term){
		setSimpleValue("term", term);
	}
}





