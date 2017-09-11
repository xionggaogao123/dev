package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott  on 2017/8/23.
 * {
 *     0:家长
 *     1:学生(未激活)
 *     2:学生(激活)
 *     3:老师
 * }
 */
public class NewVersionUserRoleEntry extends BaseDBObject {

    public NewVersionUserRoleEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public NewVersionUserRoleEntry(ObjectId userId,
                                   int newRole){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("nr",newRole)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setNewRole(int newRole){
        setSimpleValue("nr",newRole);
    }

    public int getNewRole(){
        return getSimpleIntegerValueDef("nr",-1);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
}
