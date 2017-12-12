package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/24.
 */
public class VirtualCommunityEntry extends BaseDBObject{

    public VirtualCommunityEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public VirtualCommunityEntry(ObjectId communityId,
                                 int userCount,
                                 String fileName){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("cid",communityId)
                .append("fn",fileName)
                .append("uc",userCount);
        setBaseEntry(basicDBObject);
    }

    public void setFileName(String fileName){
        setSimpleValue("fn",fileName);
    }

    public String getFileName(){
        return getSimpleStringValue("fn");
    }

    public void setUserCount(int userCount){
        setSimpleValue("uc",userCount);
    }

    public int getUserCount(){
        return getSimpleIntegerValue("uc");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
}
