package com.fulaan.overallquality.dto;

import com.pojo.overallquality.StuChangeGoodsEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by guojing on 2016/8/26.
 */
public class StuChangeGoodsDTO {
    private String id;
    private String changeDate;
    private String goodsId;
    private String goodsName;
    private String picPath;
    private String gradeId;
    private String gradeName;
    private String classId;
    private String className;
    private String userId;
    private String userName;
    private String refuseCon;
    private int changeState;
    private boolean isStu;

    public StuChangeGoodsDTO(){

    }

    public StuChangeGoodsDTO(StuChangeGoodsEntry e){
        this.id=e.getID().toString();
        this.changeDate= DateTimeUtils.convert(e.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD);
        this.goodsId=e.getGoodsId().toString();
        this.goodsId=e.getGradeId().toString();
        this.classId=e.getClassId().toString();
        this.userId=e.getUserId().toString();
        this.changeState=e.getChangeState();
        if(e.getChangeState()!=0){
            this.refuseCon=e.getRefuseCon();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
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

    public String getRefuseCon() {
        return refuseCon;
    }

    public void setRefuseCon(String refuseCon) {
        this.refuseCon = refuseCon;
    }

    public int getChangeState() {
        return changeState;
    }

    public void setChangeState(int changeState) {
        this.changeState = changeState;
    }

    public boolean getIsStu() {
        return isStu;
    }

    public void setIsStu(boolean isStu) {
        this.isStu = isStu;
    }
}
