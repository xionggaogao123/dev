package com.fulaan.reportCard.dto;

/**
 * Created by scott on 2017/10/16.
 */
public class ExamGroupDTO {

    private String id;
    private String groupId;
    private String communityId;
    private String examType;
    private int recordScoreType;
    private int lrType;
    private String examName;
    private String subjectId;
    private int maxScore;
    private int qualifyScore;
    private int excellentScore;
    private String examStrTime;
    private int showType;
    private int pmType;
     

    public ExamGroupDTO(){

    }

    public GroupExamDetailDTO buildDTO(){
        GroupExamDetailDTO detailDTO=new GroupExamDetailDTO();
        detailDTO.setId(this.id);
        detailDTO.setGroupId(this.groupId);
        detailDTO.setCommunityId(this.communityId);
        detailDTO.setExamType(this.examType);
        detailDTO.setRecordScoreType(this.recordScoreType);
        detailDTO.setExamName(this.examName);
        detailDTO.setSubjectId(this.subjectId);
        detailDTO.setMaxScore(this.maxScore);
        detailDTO.setQualifyScore(this.qualifyScore);
        detailDTO.setExcellentScore(this.excellentScore);
        detailDTO.setExamStrTime(this.examStrTime);
        detailDTO.setShowType(this.showType);
        detailDTO.setLrType(lrType);
        detailDTO.setPmType(pmType);
        return detailDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    
    

    public int getPmType() {
        return pmType;
    }

    public void setPmType(int pmType) {
        this.pmType = pmType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    
    
    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getQualifyScore() {
        return qualifyScore;
    }

    public void setQualifyScore(int qualifyScore) {
        this.qualifyScore = qualifyScore;
    }

    public int getExcellentScore() {
        return excellentScore;
    }

    public void setExcellentScore(int excellentScore) {
        this.excellentScore = excellentScore;
    }

    public String getExamStrTime() {
        return examStrTime;
    }

    public void setExamStrTime(String examStrTime) {
        this.examStrTime = examStrTime;
    }

    public int getLrType() {
        return lrType;
    }

    public void setLrType(int lrType) {
        this.lrType = lrType;
    }
    
    
}
