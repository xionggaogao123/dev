package com.fulaan.reportCard.dto;

/**
 * Created by scott on 2017/10/16.
 */
public class ExamGroupNewDto {

    //id
    private String id;
    //群组id
    private String groupId;
    //班级id
    private String communityId;
    //考试类型
    private int recordScoreType;
    //考试名称
    private String examName;
    //考试id
    private String subjectIds;
    //考试时间
    private String examStrTime;

     

    public ExamGroupNewDto(){

    }

    public GroupExamDetailDTO buildDTO(){
        GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
        detailDTO.setId(this.id);
        detailDTO.setSubjectIds(subjectIds);
        detailDTO.setGroupId(this.groupId);
        detailDTO.setCommunityId(this.communityId);
        detailDTO.setRecordScoreType(this.recordScoreType);
        detailDTO.setExamName(this.examName);
        detailDTO.setExamStrTime(this.examStrTime);
        return detailDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public int getRecordScoreType() {
        return recordScoreType;
    }

    public void setRecordScoreType(int recordScoreType) {
        this.recordScoreType = recordScoreType;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getExamStrTime() {
        return examStrTime;
    }

    public void setExamStrTime(String examStrTime) {
        this.examStrTime = examStrTime;
    }

    
    
    
}

