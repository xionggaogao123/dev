package com.fulaan.backstage.dto;

import com.pojo.backstage.UserLogResultEntry;

/**
 * Created by scott on 2017/12/4.
 */
public class UserLogResultDTO {

    private String id;
    private String userId;
    private String userName;
    private int userRole;


    /**
     * 新增显示字段
     */
    //用户相关信息
    private String nickName;
    private String registerTime;
    private String jiaId;
    //roleId关联信息
    private UserRoleJurisdictionDto userRoleJurisdictionDto;
    //UserRoleJurisdictionDto 需展示的属性
    private String roleId;
    private String roleName;
    private String level;
    private String roleJurisdictionName;


    public UserLogResultDTO(){

    }

    public UserLogResultDTO(UserLogResultEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getUserId().toString();
        this.userRole=entry.getRole();
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

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public UserRoleJurisdictionDto getUserRoleJurisdictionDto() {
        return userRoleJurisdictionDto;
    }

    public void setUserRoleJurisdictionDto(UserRoleJurisdictionDto userRoleJurisdictionDto) {
        this.userRoleJurisdictionDto = userRoleJurisdictionDto;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRoleJurisdictionName() {
        return roleJurisdictionName;
    }

    public void setRoleJurisdictionName(String roleJurisdictionName) {
        this.roleJurisdictionName = roleJurisdictionName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getJiaId() {
        return jiaId;
    }

    public void setJiaId(String jiaId) {
        this.jiaId = jiaId;
    }
}
