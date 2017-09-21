package com.fulaan.newVersionBind.dto;

import com.pojo.user.NewVersionBindRelationEntry;

/**
 * Created by scott on 2017/9/8.
 */
public class NewVersionBindRelationDTO {

    private String bindId;

    private String mainUserId;
    private String userId;
    private int sex;
    private String birthDate;
    private String provinceName;
    private String regionName;
    private String regionAreaName;
    private int relation;
    private String schoolName;
    private String avatar;
    private int gradeType;
    private String nickName;
    private String mobileNumber;

    private int isBindCommunity;

    public NewVersionBindRelationDTO(NewVersionBindRelationEntry  entry){
        this.bindId=entry.getID().toString();
        this.mainUserId=entry.getMainUserId().toString();
        this.userId=entry.getUserId().toString();
        this.provinceName=entry.getProvinceName();
        this.regionName=entry.getRegionName();
        this.regionAreaName=entry.getRegionAreaName();
        this.relation=entry.getRelation();
        this.schoolName=entry.getSchoolName();
    }

    public NewVersionBindRelationDTO(
                        String bindId,
                        String mainUserId,
                        String userId,
                        int sex,
                        String birthDate,
                        String provinceName,
                        String regionName,
                        String regionAreaName,
                        int relation,
                        String schoolName,
                        String avatar,
                        int gradeType,
                        String nickName){
        this.bindId=bindId;
        this.mainUserId=mainUserId;
        this.userId=userId;
        this.sex=sex;
        this.birthDate=birthDate;
        this.provinceName=provinceName;
        this.regionName=regionName;
        this.regionAreaName=regionAreaName;
        this.relation=relation;
        this.schoolName=schoolName;
        this.avatar=avatar;
        this.gradeType=gradeType;
        this.nickName=nickName;
    }

    public NewVersionBindRelationDTO(){

    }

    public int getIsBindCommunity() {
        return isBindCommunity;
    }

    public void setIsBindCommunity(int isBindCommunity) {
        this.isBindCommunity = isBindCommunity;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionAreaName() {
        return regionAreaName;
    }

    public void setRegionAreaName(String regionAreaName) {
        this.regionAreaName = regionAreaName;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGradeType() {
        return gradeType;
    }

    public void setGradeType(int gradeType) {
        this.gradeType = gradeType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
