package com.pojo.studentattendance;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 学生考勤信息
 * <pre>
 * collectionName:stu_attendance
 * </pre>
 * <pre>
 * {
 *  gid:年级id
 *  cid:班级id
 * 	sid:学生id
 * 	sn:学生姓名
 *  ast:考勤状态 	0:正常
 *  de:考勤日期
 *  ude:更新日期
 *  rmk:备注
 * }
 * </pre>
 * @author xusy 2016-11-08 15:29:47
 */
public class StudentAttendanceEntry extends BaseDBObject {

	private static final long serialVersionUID = -4291689065891095516L;
	
	public StudentAttendanceEntry() {
		super();
	}
	
	public StudentAttendanceEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public StudentAttendanceEntry(ObjectId gradeId, ObjectId clazzId,
			ObjectId studentId, String studentName, 
			int attendanceStatus, String attendanceDate, 
			long updateTime, String remark) {
		
		BasicDBObject basicObj = new BasicDBObject();
		basicObj.append("gid", gradeId)
			.append("cid", clazzId)
			.append("sid", studentId)
			.append("sn", studentName)
			.append("ast", attendanceStatus)
			.append("de", attendanceDate)
			.append("ude", updateTime)
			.append("rmk", remark);
		
		setBaseEntry(basicObj);
	}
	
	public ObjectId getGradeId() {
		return getSimpleObjecIDValue("gid");
	}
	
	public void setGradeId(ObjectId gradeId) {
		setSimpleValue("gid", gradeId);
	}
	
	public ObjectId getClazzId() {
		return getSimpleObjecIDValue("cid");
	}
	
	public void setClazzId(ObjectId clazzId) {
		setSimpleValue("cid", clazzId);
	}
	
	public ObjectId getStudentId() {
		return getSimpleObjecIDValue("sid");
	}
	
	public void setStudentId(ObjectId studentId) {
		setSimpleValue("sid", studentId);
	}
	
	public String getStudentName() {
		return getSimpleStringValue("sn");
	}
	
	public void setStudentName(String stuName) {
		setSimpleValue("sn", stuName);
	}
	
	public int getStuAttendanceStatus() {
		return getSimpleIntegerValue("ast");
	}
	
	public void setStuAttendanceStatus(int attendanceStatus) {
		setSimpleValue("ast", attendanceStatus);
	}
	
	public String getAttendanceDate() {
		return getSimpleStringValue("de");
	}
	
	public void setAttendanceDate(String attendanceDate) {
		setSimpleValue("de", attendanceDate);
	}
	
	public long getAttendanceUpdateTime() {
		return getSimpleLongValue("ude");
	}
	
	public void setAttendanceUpdateTime(long attendanceUpdateTime) {
		setSimpleValue("ude", attendanceUpdateTime);
	}
	
	public String getRemark() {
		return getSimpleStringValue("rmk");
	}
	
	public void setRemark(String remark) {
		setSimpleValue("rmk", remark);
	}
	
}
