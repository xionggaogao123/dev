package com.fulaan.reportCard.dto;

import com.pojo.reportCard.MultiGroupExamDetailEntry;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.TimeChangeUtils;

public class MultiGroupExamDetailDto {

    private String id;
    
    private String examName;
    
    private String communityName;
    
    //分数制
    private Integer recordScoreType;
    
    private String communityId;
    
    private String examTime;
    
    private String subjectName;
    
    private String subjectIds;
    
    private String fbDate;
    //0：保存
    private Integer status;
    
    private String grade;
    
    private boolean isOwn;
    
    
    
    public MultiGroupExamDetailDto() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public MultiGroupExamDetailDto(MultiGroupExamDetailEntry entry) {
        this.id = entry.getID().toString();
        this.examName = entry.getExamName();
        this.communityName = entry.getCName();
        this.examTime = DateTimeUtils.convert(entry.getExamTime(),
            DateTimeUtils.DATE_YYYY_MM_DD);
        this.fbDate = TimeChangeUtils.getChangeTime(entry.getSubmitTime());
        this.status = entry.getStatus();
        this.grade = entry.getGrade();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExamName() {
        return examName;
    }
    public void setExamName(String examName) {
        this.examName = examName;
    }
    public String getCommunityName() {
        return communityName;
    }
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
    public String getExamTime() {
        return examTime;
    }
    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getFbDate() {
        return fbDate;
    }
    public void setFbDate(String fbDate) {
        this.fbDate = fbDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public Integer getRecordScoreType() {
        return recordScoreType;
    }

    public void setRecordScoreType(Integer recordScoreType) {
        this.recordScoreType = recordScoreType;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public boolean isOwn() {
        return isOwn;
    }

    public void setOwn(boolean isOwn) {
        this.isOwn = isOwn;
    }
    
    
}
