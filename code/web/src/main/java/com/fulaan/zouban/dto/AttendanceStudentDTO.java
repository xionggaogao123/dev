package com.fulaan.zouban.dto;

import com.pojo.zouban.AttendanceEntry;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/5/6.
 */
public class AttendanceStudentDTO {
    private String studentId;
    private String studentName;
    private int attendance;
    private int score1;
    private int score2;
    private int score3;

    public AttendanceStudentDTO(){}

    public AttendanceStudentDTO(AttendanceEntry.Student student){
        this.studentId = student.getStudentId().toString();
        this.studentName = student.getStudentName();
        this.attendance = student.getAttendance();
        this.score1 = student.getScore1();
        this.score2 = student.getScore2();
        this.score3 = student.getScore3();
    }

    public AttendanceEntry.Student exportEntry(){
        AttendanceEntry.Student student = new AttendanceEntry.Student();
        student.setStudentId(new ObjectId(studentId));
        student.setStudentName(studentName);
        student.setAttendance(attendance);
        student.setScore1(score1);
        student.setScore2(score2);
        student.setScore3(score3);
        return student;
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

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score) {
        this.score1 = score;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getScore3() {
        return score3;
    }

    public void setScore3(int score3) {
        this.score3 = score3;
    }
}
