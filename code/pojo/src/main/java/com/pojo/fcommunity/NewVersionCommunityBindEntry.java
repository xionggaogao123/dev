package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/13.
 */
public class NewVersionCommunityBindEntry extends BaseDBObject{

    public NewVersionCommunityBindEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }


    public NewVersionCommunityBindEntry(ObjectId communityId,
                                        ObjectId mainUserId,
                                        ObjectId userId,
                                        String thirdName,
                                        String number){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId)
                .append("uid",userId)
                .append("tn",thirdName)
                .append("nm",number)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public NewVersionCommunityBindEntry(ObjectId communityId,
                                        ObjectId mainUserId,
                                        ObjectId userId){
        BasicDBObject basicDBObject = new BasicDBObject()
                 .append("cid",communityId)
                 .append("muid",mainUserId)
                 .append("uid",userId)
                 .append("tn",Constant.EMPTY)
                 .append("nm",Constant.EMPTY)
                 .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setThirdName(String thirdName){
        setSimpleValue("tn",thirdName);
    }

    public String getThirdName(){
        return getSimpleStringValue("tn");
    }

    public void setNumber(String number){
        setSimpleValue("nm",number);
    }

    public String getNumber(){
        return getSimpleStringValue("nm");
    }

    public int getRemoveStatus(){
        return getSimpleIntegerValue("ir");
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public void setMainUserId(ObjectId mainUserId){
         setSimpleValue("muid",mainUserId);
    }

    public ObjectId getMainUserId(){
        return getSimpleObjecIDValue("muid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
}
