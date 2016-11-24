package com.fulaan.zouban.dto;

import com.pojo.zouban.CourseItem;
import com.pojo.zouban.TimeTableEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/9/15.
 */
public class TimeTableDTO {
    private String id;
    private String schoolId;
    private String term;
    private String gradeId;
    private String classId;
    private List<CourseItemDTO> courseList;
    private int type;
    private int lock;
    private int week;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<CourseItemDTO> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseItemDTO> courseList) {
        this.courseList = courseList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public TimeTableDTO() {
    }

    public TimeTableDTO(TimeTableEntry timeTableEntry) {
        this.id = timeTableEntry.getID().toString();
        this.schoolId = timeTableEntry.getSchoolId().toString();
        this.term = timeTableEntry.getTerm();
        this.gradeId = timeTableEntry.getGradeId().toString();
        this.classId = timeTableEntry.getClassId().toString();
        List<CourseItem> courseItemList = timeTableEntry.getCourseList();
        List<CourseItemDTO> courseItemDTOs = new ArrayList<CourseItemDTO>();
        if (courseItemList != null && !courseItemList.isEmpty()) {
            for (CourseItem courseItem : courseItemList) {
                courseItemDTOs.add(new CourseItemDTO(courseItem));
            }
        }
        this.courseList = courseItemDTOs;
        this.type = timeTableEntry.getType();
        this.lock = timeTableEntry.getLock();
        this.week = timeTableEntry.getWeek();
    }

}
