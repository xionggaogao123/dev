package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2018/1/5.
 */
public class RecordParentImportEntry extends BaseDBObject{

    public RecordParentImportEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public RecordParentImportEntry(ObjectId parentId,
                                   String userKey,
                                   String nickName){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("pid",parentId)
                .append("uk",userKey)
                .append("nnm",nickName)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setNickName(String nickName){
        setSimpleValue("nnm",nickName);
    }

    public String getNickName(){
        return getSimpleStringValue("nnm");
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
