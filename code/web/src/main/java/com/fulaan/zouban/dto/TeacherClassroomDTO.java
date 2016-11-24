package com.fulaan.zouban.dto;

import com.fulaan.examresult.controller.IdNameDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/21.
 */
public class TeacherClassroomDTO {
    private String courseId;
    private String courseName;
    private int studentCount;
    private String teacherId;
    private String teacherName;
    private List<IdNameDTO> teacherList = new ArrayList<IdNameDTO>();
    private String classroomId;
    private List<IdNameDTO> classroomList = new ArrayList<IdNameDTO>();


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
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

    public List<IdNameDTO> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<IdNameDTO> teacherList) {
        this.teacherList = teacherList;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public List<IdNameDTO> getClassroomList() {
        return classroomList;
    }

    public void setClassroomList(List<IdNameDTO> classroomList) {
        this.classroomList = classroomList;
    }
}
