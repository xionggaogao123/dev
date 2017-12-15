package com.pojo.appnotice;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/15.
 */
public class TransferCommunityRecordEntry extends BaseDBObject{

    public TransferCommunityRecordEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public TransferCommunityRecordEntry(ObjectId communityId,
                                        ObjectId cOwnerId,
                                        ObjectId gOwnerId){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("cid",communityId)
                .append("co",cOwnerId)
                .append("go",gOwnerId);
        setBaseEntry(basicDBObject);
    }


    public void setGOwnerId(ObjectId gOwnerId){
        setSimpleValue("go",gOwnerId);
    }


    public ObjectId getGOwnerId(){
        return getSimpleObjecIDValue("go");
    }

    public void setCOwnerId(ObjectId cOwnerId){
        setSimpleValue("co",cOwnerId);
    }


    public ObjectId getCOwnerId(){
        return getSimpleObjecIDValue("co");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
}
