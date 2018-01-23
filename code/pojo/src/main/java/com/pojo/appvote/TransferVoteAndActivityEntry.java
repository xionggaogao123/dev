package com.pojo.appvote;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2018/1/22.
 */
public class TransferVoteAndActivityEntry extends BaseDBObject{

    public TransferVoteAndActivityEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public TransferVoteAndActivityEntry(ObjectId transferId,
                                        ObjectId newItemId,
                                        int type){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("tfi",transferId)
                .append("ty",type)
                .append("nid",newItemId);
        setBaseEntry(basicDBObject);
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setNewItemId(ObjectId newItemId){
        setSimpleValue("nid",newItemId);
    }

    public ObjectId getNewItemId(){
        return getSimpleObjecIDValue("nid");
    }

    public ObjectId getTransferId(){
        return getSimpleObjecIDValue("tfi");
    }

    public void setTransferId(ObjectId transferId){
        setTransferId(transferId);
    }
}
