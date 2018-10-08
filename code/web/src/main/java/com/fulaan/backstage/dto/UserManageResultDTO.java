package com.fulaan.backstage.dto;


import java.util.List;

/**
 * 用户管理 列表展示Dto
 * Created by taotao.chan on 2018年8月30日13:32:17
 *
 */
public class UserManageResultDTO {

    private String id;
    private String userId;
    private String userName;
    private String nickName;
    private String telephone;

    //用户角色
    private String userRoleName;
    //家校美ID
    private String jiaId;

    //UserRoleJurisdictionDto 需展示的属性
    //系统中角色
    private String sysRoleName;

    //在线或登录状态
    private String lineStatus;

    //相关社群 个数
    private String communityCount;

    //小孩信息
    private List<UserManageChildrenDTO> childrenDTOList;

    //父母信息
    private List<UserManageParentDTO> parentDTOList;

    public UserManageResultDTO(){

    }

//    public UserManageResultDTO(UserLogResultEntry entry){
//        this.id=entry.getID().toString();
//        this.userId=entry.getUserId().toString();
//    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getSysRoleName() {
        return sysRoleName;
    }

    public void setSysRoleName(String sysRoleName) {
        this.sysRoleName = sysRoleName;
    }

    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }

    public String getCommunityCount() {
        return communityCount;
    }

    public void setCommunityCount(String communityCount) {
        this.communityCount = communityCount;
    }

    public List<UserManageChildrenDTO> getChildrenDTOList() {
        return childrenDTOList;
    }

    public void setChildrenDTOList(List<UserManageChildrenDTO> childrenDTOList) {
        this.childrenDTOList = childrenDTOList;
    }

    public List<UserManageParentDTO> getParentDTOList() {
        return parentDTOList;
    }

    public void setParentDTOList(List<UserManageParentDTO> parentDTOList) {
        this.parentDTOList = parentDTOList;
    }

    public String getJiaId() {
        return jiaId;
    }

    public void setJiaId(String jiaId) {
        this.jiaId = jiaId;
    }
}
