package com.fulaan.zouban.dto;


import java.util.Map;


/**
 * Created by qiangm on 2015/10/12.
 */
public class GradeSubjectCourse {
    String gradeId;
    String gradeName;

    Map<String,Map<String,String>> groupInfo;
    //Map<String,String> classInfo;

    public GradeSubjectCourse() {
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Map<String, Map<String, String>> getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(Map<String, Map<String, String>> groupInfo) {
        this.groupInfo = groupInfo;
    }
}
