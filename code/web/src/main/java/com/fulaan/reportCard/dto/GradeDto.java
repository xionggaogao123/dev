package com.fulaan.reportCard.dto;

public class GradeDto {

    private String gradeVal;
    
    private String gradeName;
    
    public GradeDto(String gradeVal, String gradeName) {
        this.gradeVal = gradeVal;
        this.gradeName = gradeName;
    }

    public String getGradeVal() {
        return gradeVal;
    }
    
    

    public void setGradeVal(String gradeVal) {
        this.gradeVal = gradeVal;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
    
    
}
