package com.fulaan.zouban.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/19.
 */
public class SubjectCourseDTO {
    private String subjectName;
    private List<String> advCourseList = new ArrayList<String>();
    private List<String> simCourseList = new ArrayList<String>();


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<String> getAdvCourseList() {
        return advCourseList;
    }

    public void setAdvCourseList(String advCourse) {
        this.advCourseList.add(advCourse);
    }

    public List<String> getSimCourseList() {
        return simCourseList;
    }

    public void setSimCourseList(String simCourse) {
        this.simCourseList.add(simCourse);
    }

    public int getCount() {
        return advCourseList.size() + simCourseList.size();
    }
}
