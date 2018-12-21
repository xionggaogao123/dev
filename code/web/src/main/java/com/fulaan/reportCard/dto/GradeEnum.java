package com.fulaan.reportCard.dto;

public enum GradeEnum {

    GRADEONE("一年级","1"),
    GRADETWO("二年级","2"),
    GRADETHREE("三年级","3"),
    GRADEFOUR("四年级","4"),
    GRADEFIVE("五年级","5"),
    GRADESIX("六年级","6"),
    GRADESEVEN("初一年级","7"),
    GRADEEIGHT("初二年级","8"),
    GRADENINE("初三年级","9"),
    GRADETEN("高一年级","10"),
    GRADEELEVEN("高二年级","11"),
    GRADETWELVE("高三年级","12");
    
    private String gradeValue;
    private String gradeName;
    
    private GradeEnum(String gradeName, String gradeValue) {
        this.gradeValue = gradeValue;
        this.gradeName = gradeName;
    }
    
    public String getGradeValue() {
        return this.gradeValue;
    }
    
    public String getGradeName() {
        return this.gradeName;
    }
    
    
    
    
}
