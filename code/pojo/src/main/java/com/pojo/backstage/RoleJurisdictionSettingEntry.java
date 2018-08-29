package com.pojo.backstage;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoto.chan on 2018年8月22日16:56:14
 *
 * 用户角色权限设置页面
 *
 * id		主键                         ObjectId
 * roleName		角色名称                String
 * jurisdictionLevelId  权限管理表Id       ObjectId
 * roleProperty          角色属性    String
 * createBy          创建人            ObjectId
 * creationDate      创建时间         String
 * lastUpdateBy      最新更新人       ObjectId
 * lastUpdateDate    最新更新时间     String
 *
 */
public class RoleJurisdictionSettingEntry extends BaseDBObject{

    public RoleJurisdictionSettingEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public RoleJurisdictionSettingEntry(String roleName,
                                        ObjectId jurisdictionLevelId,
                                        String roleProperty,
                                        ObjectId createBy,
                                        String creationDate,
                                        ObjectId lastUpdateBy,
                                        String lastUpdateDate
                                     ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("roleName", roleName)
                .append("jurisdictionLevelId", jurisdictionLevelId)
                .append("roleProperty", roleProperty)
                .append("createBy", createBy)
                .append("creationDate", creationDate)
                .append("lastUpdateBy", lastUpdateBy)
                .append("lastUpdateDate", lastUpdateDate)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }


    public String getRoleName() {
        return getSimpleStringValue("roleName");
    }

    public void setRoleName(String roleName) {
        setSimpleValue("roleName",roleName);
    }

    public ObjectId getJurisdictionLevelId() {
        return getSimpleObjecIDValue("jurisdictionLevelId");
    }

    public void setJurisdictionLevelId(ObjectId jurisdictionLevelId) {
        setSimpleValue("jurisdictionLevelId",jurisdictionLevelId);
    }

    public String getRoleProperty() {
        return getSimpleStringValue("roleProperty");
    }

    public void setRoleProperty(String roleProperty) {
        setSimpleValue("roleProperty",roleProperty);
    }


    public ObjectId getCreateBy() {
        return getSimpleObjecIDValue("createBy");
    }

    public void setCreateBy(ObjectId createBy) {
        setSimpleValue("createBy",createBy);
    }

    public String getCreationDate() {
        return getSimpleStringValue("creationDate");
    }

    public void setCreationDate(String creationDate) {
        setSimpleValue("creationDate",creationDate);
    }

    public ObjectId getLastUpdateBy() {
        return getSimpleObjecIDValue("lastUpdateBy");
    }

    public void setLastUpdateBy(ObjectId lastUpdateBy) {
        setSimpleValue("lastUpdateBy",lastUpdateBy);
    }

    public String getLastUpdateDate() {
        return getSimpleStringValue("lastUpdateDate");
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        setSimpleValue("lastUpdateDate",lastUpdateDate);
    }

}
