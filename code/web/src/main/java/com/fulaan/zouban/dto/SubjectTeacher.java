package com.fulaan.zouban.dto;

import com.fulaan.examresult.controller.IdNameDTO;

import java.util.List;

/**
 * Created by qiangm on 2015/10/13.
 */
public class SubjectTeacher {
    private String subjectId;
    private String subjectName;
    private List<IdNameDTO> teacherList;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<IdNameDTO> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<IdNameDTO> teacherList) {
        this.teacherList = teacherList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectTeacher)) return false;

        SubjectTeacher that = (SubjectTeacher) o;

        if (!subjectId.equals(that.subjectId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return subjectId.hashCode();
    }
}
