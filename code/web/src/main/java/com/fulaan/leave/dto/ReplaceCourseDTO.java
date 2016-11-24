package com.fulaan.leave.dto;

import com.pojo.leave.ReplaceCourse;
import org.bson.types.ObjectId;

/**
 * 老师代课dto
 * Created by qiangm on 2016/3/4.
 */
public class ReplaceCourseDTO {
    private String id;
    private String schoolId;
    private String teacherId;
    private String courseId;
    private String teacherName;
    private String courseName;
    private int week;
    private int x;
    private int y;
    private String oldTeacherId;
    private String oldTeacherName;
    private String managerId;
    private String managerName;
    private String term;
    private String time;
    private String leaveId;

    public ReplaceCourseDTO() {
    }

    public ReplaceCourseDTO(String id, String schoolId, String teacherId, String teacherName,
                            String courseId,String courseName,int week, int x,
                            int y, String oldTeacherId, String oldTeacherName,
                            String managerId, String managerName,String term,String leaveId) {
        this.id = id;
        this.schoolId = schoolId;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.courseId = courseId;
        this.courseName=courseName;
        this.week = week;
        this.x = x;
        this.y = y;
        this.oldTeacherId = oldTeacherId;
        this.oldTeacherName = oldTeacherName;
        this.managerId = managerId;
        this.managerName = managerName;
        this.term=term;
        this.leaveId=leaveId;
    }
    public ReplaceCourseDTO(ReplaceCourse rp)
    {
        this.id=rp.getID().toString();
        this.schoolId=rp.getSchoolId().toString();
        this.teacherId=rp.getTeacherId().toString();
        this.courseId=rp.getCourseId().toString();
        this.courseName=rp.getCourseName();
        this.week=rp.getWeek();
        this.x=rp.getX();
        this.y=rp.getY();
        this.oldTeacherId=rp.getOldTeacherId().toString();
        this.managerId=rp.getManagerId().toString();
        this.term=rp.getTerm();
        this.leaveId=rp.getLeaveId().toString();
    }
    public ReplaceCourse export()
    {
        ReplaceCourse rp=new ReplaceCourse();
        rp.setSchoolId(new ObjectId(this.getSchoolId()));
        rp.setTeacherId(new ObjectId(this.getTeacherId()));
        rp.setCourseId(new ObjectId(this.getCourseId()));
        rp.setCourseName(this.getCourseName());
        rp.setWeek(this.getWeek());
        rp.setX(this.getX());
        rp.setY(this.getY());
        rp.setOldTeacherId(new ObjectId(this.getOldTeacherId()));
        rp.setManagerId(new ObjectId(this.getManagerId()));
        rp.setTerm(this.getTerm());
        rp.setLeaveId(new ObjectId(this.getLeaveId()));
        return rp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getOldTeacherId() {
        return oldTeacherId;
    }

    public void setOldTeacherId(String oldTeacherId) {
        this.oldTeacherId = oldTeacherId;
    }

    public String getOldTeacherName() {
        return oldTeacherName;
    }

    public void setOldTeacherName(String oldTeacherName) {
        this.oldTeacherName = oldTeacherName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }
}
