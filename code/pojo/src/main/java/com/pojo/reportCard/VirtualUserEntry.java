package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/13.
 * 虚拟账户表
 */
public class VirtualUserEntry extends BaseDBObject{

    public VirtualUserEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public VirtualUserEntry(ObjectId communityId,
                            String userNumber,
                            ObjectId userId,
                            String userName){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("cid",communityId)
                .append("unm",userName)
                .append("uid",userId)
                .append("un",userNumber);
        setBaseEntry(basicDBObject);
    }

    public void setUserNumber(String userNumber){
        setSimpleValue("un",userNumber);
    }

    public String getUserNumber(){
        return getSimpleStringValue("un");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserName(String userName){
        setSimpleValue("unm",userName);
    }

    public String getUserName(){
        return getSimpleStringValue("unm");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }


}
