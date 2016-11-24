package com.fulaan.quality.dto;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/11/11.
 */
public class TeacherCheckDTO {

    private String userId;

    private String userName;

    private String subjectId;

    private String subjectName;

    private String term;

    private List<TeacherProjectDTO> teacherProjectDTOList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<TeacherProjectDTO> getTeacherProjectDTOList() {
        return teacherProjectDTOList;
    }

    public void setTeacherProjectDTOList(List<TeacherProjectDTO> teacherProjectDTOList) {
        this.teacherProjectDTOList = teacherProjectDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeacherCheckDTO that = (TeacherCheckDTO) o;

        if (!subjectId.equals(that.subjectId)) return false;
        if (!userId.equals(that.userId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + subjectId.hashCode();
        return result;
    }
}
