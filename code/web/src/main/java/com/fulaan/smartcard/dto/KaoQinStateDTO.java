package com.fulaan.smartcard.dto;

import com.pojo.smartcard.KaoQinStateEntry;

/**
 * Created by guojing on 2016/6/20.
 */
public class KaoQinStateDTO {
    private String id;
    private String userId;
    private String userName;
    private String gradeId;
    private String gradeName;
    private String classId;
    private String className;
    private int normalCount;
    private String normalReta;
    private int lateCount;
    private String lateReta;
    private int punctualCount;
    private String punctualReta;
    private int kuangkeCount;
    private String kuangkeReta;

    public KaoQinStateDTO(){

    }

    public KaoQinStateDTO(KaoQinStateEntry e){
        this.id=e.getID().toString();
        this.userId=e.getUserId().toString();
        this.normalCount=e.getNormalCount();
        this.lateCount=e.getLateCount();
        this.punctualCount=e.getPunctualCount();
        this.kuangkeCount=e.getKuangkeCount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(int normalCount) {
        this.normalCount = normalCount;
    }

    public String getNormalReta() {
        return normalReta;
    }

    public void setNormalReta(String normalReta) {
        this.normalReta = normalReta;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public String getLateReta() {
        return lateReta;
    }

    public void setLateReta(String lateReta) {
        this.lateReta = lateReta;
    }

    public int getPunctualCount() {
        return punctualCount;
    }

    public void setPunctualCount(int punctualCount) {
        this.punctualCount = punctualCount;
    }

    public String getPunctualReta() {
        return punctualReta;
    }

    public void setPunctualReta(String punctualReta) {
        this.punctualReta = punctualReta;
    }

    public int getKuangkeCount() {
        return kuangkeCount;
    }

    public void setKuangkeCount(int kuangkeCount) {
        this.kuangkeCount = kuangkeCount;
    }

    public String getKuangkeReta() {
        return kuangkeReta;
    }

    public void setKuangkeReta(String kuangkeReta) {
        this.kuangkeReta = kuangkeReta;
    }
}
