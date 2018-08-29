package com.pojo.backstage;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
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
 * level		权限级别                String
 * roleJurisdiction  角色权限（）       List<String>
 * rolePath          角色可访问路径    List<String>
 * createBy          创建人            ObjectId
 * creationDate      创建时间         String
 * lastUpdateBy      最新更新人       ObjectId
 * lastUpdateDate    最新更新时间     String
 *
 */
public class UserRoleJurisdictionEntry extends BaseDBObject{

    public UserRoleJurisdictionEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public UserRoleJurisdictionEntry(String level,
                                     List<String> roleJurisdiction,
                                     List<String> rolePath,
                                     ObjectId createBy,
                                     String creationDate,
                                     ObjectId lastUpdateBy,
                                     String lastUpdateDate
                                     ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("level", level)
                .append("roleJurisdiction", roleJurisdiction)
                .append("rolePath", rolePath)
                .append("createBy", createBy)
                .append("creationDate", creationDate)
                .append("lastUpdateBy", lastUpdateBy)
                .append("lastUpdateDate", lastUpdateDate)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

//    public void setPaths(List<String> paths){
//        setSimpleValue("ps",paths);
//    }
//
//    public List<String> getPaths(){
//        List<String> paths=new ArrayList<String>();
//        BasicDBList list=(BasicDBList)getSimpleObjectValue("ps");
//        if(null!=list&&!list.isEmpty()){
//            for(Object o:list){
//                paths.add((String)o);
//            }
//        }
//        return paths;
//    }
//

    public String getLevel() {
        return getSimpleStringValue("level");
    }

    public void setLevel(String level) {
        setSimpleValue("level",level);
    }

    public List<String> getRoleJurisdiction() {
        List<String> jurisdictionList=new ArrayList<String>();
        BasicDBList list=(BasicDBList)getSimpleObjectValue("roleJurisdiction");
        if(null!=list&&!list.isEmpty()){
            for(Object o:list){
                jurisdictionList.add((String)o);
            }
        }
        return jurisdictionList;
    }

    public void setRoleJurisdiction(List<String> roleJurisdiction) {
        setSimpleValue("roleJurisdiction",roleJurisdiction);
    }

    public List<String> getRolePath() {
        List<String> pathList=new ArrayList<String>();
        BasicDBList list=(BasicDBList)getSimpleObjectValue("rolePath");
        if(null!=list&&!list.isEmpty()){
            for(Object o:list){
                pathList.add((String)o);
            }
        }
        return pathList;
    }

    public void setRolePath(List<String> rolePath) {
        setSimpleValue("rolePath",rolePath);
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
