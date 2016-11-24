package com.fulaan.zouban.dto;

import com.pojo.zouban.CourseEvent;
import com.pojo.zouban.CourseConfEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 课表配置
 * Created by qiangm on 2015/9/15.
 */
public class CourseConfDTO implements Serializable {
    private String id;
    private String schoolId;//学校id
    private String term;//学期
    private String gradeId;//年级id
    private List<Integer> classDays;//上课星期，1~7
    private int classCount;//每天课时数
    private List<String> classTime;//每节课上课时间 08:00~08:45
    private List<CourseEventDTO> events;//不可排课事务


    public CourseConfDTO() {}

    public CourseConfDTO(TimetableConfDTO timetableConfDTO){
        this.id = timetableConfDTO.getId();
        this.schoolId = "";
        this.term = timetableConfDTO.getTerm();
        this.gradeId = timetableConfDTO.getGradeId();
        this.classDays = timetableConfDTO.getDays();
        this.classCount = timetableConfDTO.getClassCount();
        this.classTime = timetableConfDTO.getClassTime();
        this.events = new ArrayList<CourseEventDTO>();
    }

    public CourseConfDTO(CourseConfEntry courseEntry) {
        this.id = courseEntry.getID().toString();
        this.schoolId = courseEntry.getSchoolId().toString();
        this.term = courseEntry.getTerm();
        this.gradeId = courseEntry.getGradeId().toString();
        this.classDays = courseEntry.getClassDays();
        this.classCount = courseEntry.getClassCount();
        this.classTime = courseEntry.getClassTime();
        List<CourseEvent> courseEvents = courseEntry.getClassEvents();
        List<CourseEventDTO> courseEventDTOs = new ArrayList<CourseEventDTO>();
        if (courseEvents != null && !courseEvents.isEmpty()) {
            for (CourseEvent courseEvent : courseEvents) {
                courseEventDTOs.add(new CourseEventDTO(courseEvent));
            }
        }
        this.events = courseEventDTOs;
    }

    public CourseConfEntry export() {
        CourseConfEntry courseEntry = new CourseConfEntry();
        if (StringUtils.isNotBlank(id)) {
            courseEntry.setID(new ObjectId(id));
        }
        courseEntry.setSchoolId(new ObjectId(schoolId));
        courseEntry.setTerm(term);
        courseEntry.setGradeId(new ObjectId(this.getGradeId()));
        courseEntry.setClassDays(this.getClassDays());
        courseEntry.setClassCount(this.classCount);
        courseEntry.setClassTimeLists(this.getClassTime());
        List<CourseEventDTO> courseEventDTOs = this.getEvents();
        List<CourseEvent> courseEvents = new ArrayList<CourseEvent>();
        if (courseEventDTOs != null && !courseEventDTOs.isEmpty()) {
            for (CourseEventDTO courseEventDTO : courseEventDTOs) {
                courseEvents.add(courseEventDTO.export());
            }
        }
        courseEntry.setClassEvents(courseEvents);
        return courseEntry;
    }


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

    public List<Integer> getClassDays() {
        return classDays;
    }

    public void setClassDays(List<Integer> classDays) {
        this.classDays = classDays;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public List<String> getClassTime() {
        return classTime;
    }

    public void setClassTime(List<String> classTime) {
        this.classTime = classTime;
    }

    public List<CourseEventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<CourseEventDTO> events) {
        this.events = events;
    }


}
