package com.fulaan.backstage.dto;



import com.pojo.backstage.RoleJurisdictionSettingEntry;
import com.pojo.backstage.UserRoleJurisdictionEntry;

import java.util.List;

/**
 * Created by taotao.chan on 2018年8月22日16:52:32
 */
public class RoleJurisdictionSettingDto {

    private String id;
    private String roleName;
    private String jurisdictionLevelId;
    private String roleProperty;
    private String createBy;
    private String creationDate;
    private String lastUpdateBy;
    private String lastUpdateDate;

    private UserRoleJurisdictionDto userRoleJurisdictionDto;

    //UserRoleJurisdictionDto 需展示的属性
    private String level;
    private String roleJurisdictionName;



    public RoleJurisdictionSettingDto(RoleJurisdictionSettingEntry entry){
        this.id = entry.getID().toString();
        this.roleName = entry.getRoleName();
        this.jurisdictionLevelId = entry.getJurisdictionLevelId().toString();
        this.roleProperty = entry.getRoleProperty();
        this.createBy = entry.getCreateBy().toString();
        this.creationDate = entry.getCreationDate();
        this.lastUpdateBy = entry.getLastUpdateBy().toString();
        this.lastUpdateDate = entry.getLastUpdateDate();
    }

    public RoleJurisdictionSettingDto(RoleJurisdictionSettingEntry entry, UserRoleJurisdictionEntry jurisdictionEntry){
        this.id = entry.getID().toString();
        this.roleName = entry.getRoleName();
        this.jurisdictionLevelId = entry.getJurisdictionLevelId().toString();
        this.roleProperty = entry.getRoleProperty();
        this.createBy = entry.getCreateBy().toString();
        this.creationDate = entry.getCreationDate();
        this.lastUpdateBy = entry.getLastUpdateBy().toString();
        this.lastUpdateDate = entry.getLastUpdateDate();
        if (jurisdictionEntry==null){
            this.userRoleJurisdictionDto = null;
            this.level = "";
            this.roleJurisdictionName = "";

        }else {
            this.userRoleJurisdictionDto = new UserRoleJurisdictionDto(jurisdictionEntry);
            this.level = userRoleJurisdictionDto.getLevel();
            this.roleJurisdictionName = userRoleJurisdictionDto.getRoleJurisdictionName();
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getJurisdictionLevelId() {
        return jurisdictionLevelId;
    }

    public void setJurisdictionLevelId(String jurisdictionLevelId) {
        this.jurisdictionLevelId = jurisdictionLevelId;
    }

    public String getRoleProperty() {
        return roleProperty;
    }

    public void setRoleProperty(String roleProperty) {
        this.roleProperty = roleProperty;
    }

    public UserRoleJurisdictionDto getUserRoleJurisdictionDto() {
        return userRoleJurisdictionDto;
    }

    public void setUserRoleJurisdictionDto(UserRoleJurisdictionDto userRoleJurisdictionDto) {
        this.userRoleJurisdictionDto = userRoleJurisdictionDto;
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
}
