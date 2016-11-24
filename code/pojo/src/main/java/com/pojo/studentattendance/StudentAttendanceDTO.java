package com.pojo.studentattendance;

import org.bson.types.ObjectId;

public class StudentAttendanceDTO {
	
	private String id;
	
	private String gradeId;
	
	private String clazzId;
	
	private String clazzName;
	
	private String studentId;
	
	private String studentName;
	
	private String studentNumber;
	
	private int attendanceStatus;
	
	private String attendanceDate;
	
	private String remark;
	
	public StudentAttendanceDTO() { }
	
	public StudentAttendanceDTO(StudentAttendanceEntry entry) {
		this.id = entry.getID().toString();
		this.gradeId = entry.getGradeId().toString();
		this.clazzId = entry.getClazzId().toString();
		this.studentId = entry.getStudentId().toString();
		this.attendanceStatus = entry.getStuAttendanceStatus();
		this.attendanceDate = entry.getAttendanceDate();
		this.studentName = entry.getStudentName();
		this.remark = entry.getRemark();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getClazzId() {
		return clazzId;
	}

	public void setClazzId(String clazzId) {
		this.clazzId = clazzId;
	}
	
	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public int getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(int attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public String getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public StudentAttendanceEntry exportEntry() {
		
		StudentAttendanceEntry entry = new StudentAttendanceEntry();
		entry.setGradeId(new ObjectId(gradeId));
		entry.setClazzId(new ObjectId(clazzId));
		entry.setStudentId(new ObjectId(studentId));
		entry.setStuAttendanceStatus(attendanceStatus);
		entry.setAttendanceDate(attendanceDate);
		entry.setRemark(remark);
		
		return entry;
	}
	
}
