package com.fulaan.overallquality.dto;

import com.pojo.overallquality.ClassOverallQualityScore;
import com.pojo.overallquality.ClassOverallQualityScoreEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/8/28.
 */
public class ClassOverallQualityScoreDTO {
    private String id;
    private String schoolId;
    private String gradeId;
    private String classId;
    private String className;
    private int totalScore;
    private int baseCampCount;
    private List<ClassOverallQualityScoreInfo> coqsiList=new ArrayList<ClassOverallQualityScoreInfo>();
    private int castleCount;
    private int villagerCount;
    private int soldiersCount;
    private int number;
    private boolean ourClass;

    public ClassOverallQualityScoreDTO(){

    }

    public ClassOverallQualityScoreDTO(ClassOverallQualityScoreEntry e){
        this.id = e.getID().toString();
        this.schoolId = e.getSchoolId().toString();
        this.gradeId = e.getGradeId().toString();
        this.classId = e.getClassId().toString();
        this.totalScore = e.getTotalScore();
        this.baseCampCount = e.getBaseCampCount();
        if(e.getCoqsList()!=null){
            for(ClassOverallQualityScore item: e.getCoqsList()){
                ClassOverallQualityScoreInfo info = new ClassOverallQualityScoreInfo(item);
                this.coqsiList.add(info);
            }
        }
        this.castleCount = e.getCastleCount();
        this.villagerCount = e.getVillagerCount();
        this.soldiersCount = e.getSoldiersCount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getBaseCampCount() {
        return baseCampCount;
    }

    public void setBaseCampCount(int baseCampCount) {
        this.baseCampCount = baseCampCount;
    }

    public List<ClassOverallQualityScoreInfo> getCoqsiList() {
        return coqsiList;
    }

    public void setCoqsiList(List<ClassOverallQualityScoreInfo> coqsiList) {
        this.coqsiList = coqsiList;
    }

    public int getCastleCount() {
        return castleCount;
    }

    public void setCastleCount(int castleCount) {
        this.castleCount = castleCount;
    }

    public int getVillagerCount() {
        return villagerCount;
    }

    public void setVillagerCount(int villagerCount) {
        this.villagerCount = villagerCount;
    }

    public int getSoldiersCount() {
        return soldiersCount;
    }

    public void setSoldiersCount(int soldiersCount) {
        this.soldiersCount = soldiersCount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getOurClass() {
        return ourClass;
    }

    public void setOurClass(boolean ourClass) {
        this.ourClass = ourClass;
    }
}
