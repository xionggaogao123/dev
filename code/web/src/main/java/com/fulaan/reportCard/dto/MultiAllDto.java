package com.fulaan.reportCard.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiAllDto {
    
    private String multiId;

    private String examName;
    
    private String subjectNames;
    
    private List<String> subject = new ArrayList<String>();
    
    private String communityNames;
    
    private String examTime;
    
    /*//representNameType:各级代表类型
    1:优秀、良好、合格、需努力
      2：优、良、中、差
    3：A\B\C\D*/
    private Integer representNameType;
    
  //分数制
    private Integer recordScoreType;
    
    private Map<Integer, List<MultiPartDto>> multiPartDtoList;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    public String getCommunityNames() {
        return communityNames;
    }

    public void setCommunityNames(String communityNames) {
        this.communityNames = communityNames;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public Integer getRepresentNameType() {
        return representNameType;
    }

    public void setRepresentNameType(Integer representNameType) {
        this.representNameType = representNameType;
    }

    public Integer getRecordScoreType() {
        return recordScoreType;
    }

    public void setRecordScoreType(Integer recordScoreType) {
        this.recordScoreType = recordScoreType;
    }

    public Map<Integer, List<MultiPartDto>> getMultiPartDtoList() {
        return multiPartDtoList;
    }

    public void setMultiPartDtoList(Map<Integer, List<MultiPartDto>> multiPartDtoList) {
        this.multiPartDtoList = multiPartDtoList;
    }

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    public String getMultiId() {
        return multiId;
    }

    public void setMultiId(String multiId) {
        this.multiId = multiId;
    }

    

    
    

    
    
    
    
}
