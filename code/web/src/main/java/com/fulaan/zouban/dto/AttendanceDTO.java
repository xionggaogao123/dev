package com.fulaan.zouban.dto;


import com.pojo.zouban.AttendanceEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/6.
 */
public class AttendanceDTO {
    private String id;
    private String courseId;
    private String courseName;
    private String lessonName;
    private String teacherId;
    private String teacherName;
    private int count;//课程总人数
    private int attendedCount;
    private String date;
    private String week;
    private String day;
    private String section;
    private List<AttendanceStudentDTO> studentList = new ArrayList<AttendanceStudentDTO>();
    private int teacherScore;
    private int classScore;

    public AttendanceDTO(){}

    public AttendanceDTO(AttendanceEntry attendanceEntry){
        this.id = attendanceEntry.getID().toString();
        this.courseId = attendanceEntry.getCourseId().toString();
        this.courseName = attendanceEntry.getCourseName();
        this.lessonName = attendanceEntry.getLessonName();
        this.teacherId = attendanceEntry.getTeacherId().toString();
        this.teacherName = attendanceEntry.getTeacherName();
        this.attendedCount = attendanceEntry.getAttendedCount();
        this.date = attendanceEntry.getDate();
        this.week = intToStr(attendanceEntry.getWeek());
        this.day = intToStr(attendanceEntry.getDay());
        this.section = intToStr(attendanceEntry.getSection());
        if(attendanceEntry.getStudentList().size() > 0){
            for(AttendanceEntry.Student student : attendanceEntry.getStudentList()){
                AttendanceStudentDTO studentDTO = new AttendanceStudentDTO(student);
                this.studentList.add(studentDTO);
            }
        }
        this.count = studentList.size();
        this.teacherScore = attendanceEntry.getTeacherScore();
        this.classScore = attendanceEntry.getClassScore();
    }

    private String intToStr(int num){
        String numStr = "";
        switch (num){
            case 1: numStr = "一";break;
            case 2: numStr = "二";break;
            case 3: numStr = "三";break;
            case 4: numStr = "四";break;
            case 5: numStr = "五";break;
            case 6: numStr = "六";break;
            case 7: numStr = "七";break;
            case 8: numStr = "八";break;
            case 9: numStr = "九";break;
            case 10: numStr = "十";break;
            case 11: numStr = "十一";break;
            case 12: numStr = "十二";break;
            case 13: numStr = "十三";break;
            case 14: numStr = "十四";break;
            case 15: numStr = "十五";break;
            case 16: numStr = "十六";break;
            case 17: numStr = "十七";break;
            case 18: numStr = "十八";break;
            case 19: numStr = "十九";break;
            case 20: numStr = "二十";break;
            case 21: numStr = "二十一";break;
            case 22: numStr = "二十二";break;
            case 23: numStr = "二十三";break;

            default:break;
        }
        return numStr;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public int getAttendedCount() {
        return attendedCount;
    }

    public void setAttendedCount(int attendedCount) {
        this.attendedCount = attendedCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<AttendanceStudentDTO> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<AttendanceStudentDTO> studentList) {
        this.studentList = studentList;
    }

    public int getTeacherScore() {
        return teacherScore;
    }

    public void setTeacherScore(int teacherScore) {
        this.teacherScore = teacherScore;
    }

    public int getClassScore() {
        return classScore;
    }

    public void setClassScore(int classScore) {
        this.classScore = classScore;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
