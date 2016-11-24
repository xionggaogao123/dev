package com.pojo.attendance;

import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/7/18.
 */
public class Attendance {


    private String id;

    private String classId;
    private String studentId;
    private String date;
    private int time;
    private String remark;

    public Attendance()
    {

    }

    public Attendance(AttendanceEntry attendanceEntry)
    {
        this.setId(attendanceEntry.getID().toString());
        this.setStudentId(attendanceEntry.getStudentId().toString());
        this.setClassId(attendanceEntry.getClassId().toString());
        this.setDate(attendanceEntry.getDate());
        this.setTime(attendanceEntry.getTime());
        this.setRemark(attendanceEntry.getRemark());
    }

    public AttendanceEntry export()
    {
        AttendanceEntry attendanceEntry=new AttendanceEntry();
        attendanceEntry.setID(new ObjectId(this.getId()));
        attendanceEntry.setStudentId(new ObjectId(this.getStudentId()));
        attendanceEntry.setClassId(new ObjectId(this.classId));
        attendanceEntry.setDate(this.getDate());
        attendanceEntry.setTime(this.getTime());
        attendanceEntry.setRemark(this.getRemark());
        return attendanceEntry;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }


}
