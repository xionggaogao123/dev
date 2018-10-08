package com.fulaan.newVersionBind.dto;

import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.pojo.user.NewVersionBindRelationEntry;

import java.util.ArrayList;
import java.util.List;

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

    private String studentNumber;
    private String thirdName;
    private String personalSignature;

    private String loadTime;

    private boolean isLogin;

    private String bindCommunityStr;

    private int chatCount;

    private int selectTransfer;

    private int userBelongCommunitiesCount;

    private int virtualUser;

    private String communityId;

    private List<GroupOfCommunityDTO> bindCommunities = new ArrayList<GroupOfCommunityDTO>();

    //家校美ID
    private String jiaId;

    public NewVersionBindRelationDTO(NewVersionBindRelationEntry  entry){
        this.bindId=entry.getID().toString();
        this.mainUserId=entry.getMainUserId().toString();
        this.userId=entry.getUserId().toString();
        this.provinceName=entry.getProvinceName();
        this.regionName=entry.getRegionName();
        this.regionAreaName=entry.getRegionAreaName();
        this.relation=entry.getRelation();
        this.schoolName=entry.getSchoolName();
        this.personalSignature=entry.getPersonalSignature();
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

    public int getVirtualUser() {
        return virtualUser;
    }

    public void setVirtualUser(int virtualUser) {
        this.virtualUser = virtualUser;
    }

    public int getUserBelongCommunitiesCount() {
        return userBelongCommunitiesCount;
    }

    public void setUserBelongCommunitiesCount(int userBelongCommunitiesCount) {
        this.userBelongCommunitiesCount = userBelongCommunitiesCount;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public int getSelectTransfer() {
        return selectTransfer;
    }

    public void setSelectTransfer(int selectTransfer) {
        this.selectTransfer = selectTransfer;
    }

    public int getChatCount() {
        return chatCount;
    }

    public void setChatCount(int chatCount) {
        this.chatCount = chatCount;
    }

    public List<GroupOfCommunityDTO> getBindCommunities() {
        return bindCommunities;
    }

    public void setBindCommunities(List<GroupOfCommunityDTO> bindCommunities) {
        this.bindCommunities = bindCommunities;
    }

    public String getBindCommunityStr() {
        return bindCommunityStr;
    }

    public void setBindCommunityStr(String bindCommunityStr) {
        this.bindCommunityStr = bindCommunityStr;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
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

    public String getJiaId() {
        return jiaId;
    }

    public void setJiaId(String jiaId) {
        this.jiaId = jiaId;
    }
}
