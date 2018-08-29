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
 * Created by James on 2017/11/21.
 * role:角色 0:一般管理员 2:超级管理员
 */
public class UserLogResultEntry extends BaseDBObject {

    public UserLogResultEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public UserLogResultEntry(ObjectId userId,
                              int role){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("uid",userId)
                .append("rl",role)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    //新增roleId字段
    public UserLogResultEntry(ObjectId userId,
                              ObjectId roleId){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("uid",userId)
                .append("roleId",roleId)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setRole(int role){
        setSimpleValue("rl",role);
    }

    public int getRole(){
        return getSimpleIntegerValue("rl");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setRoleId(ObjectId roleId){
        setSimpleValue("roleId",roleId);
    }

    public ObjectId getRoleId(){
        return getSimpleObjecIDValue("roleId");
    }
}
