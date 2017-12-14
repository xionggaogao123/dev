package com.pojo.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/13.
 * 填写用户关系表
 */
public class MakeOutUserRelationEntry extends BaseDBObject{

    public MakeOutUserRelationEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public MakeOutUserRelationEntry(ObjectId parentId,
                                    String userKey){
        BasicDBObject basicDBObject  =new BasicDBObject()
                .append("pid",parentId)
                .append("uk", userKey);
        setBaseEntry(basicDBObject);
    }

    public void setUserKey(String userKey){
        setSimpleValue("uk",userKey);
    }

    public String getUserKey(){
        return getSimpleStringValue("uk");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
}
