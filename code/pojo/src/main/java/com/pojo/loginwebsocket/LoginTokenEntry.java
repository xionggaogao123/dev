package com.pojo.loginwebsocket;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/2.
 */
public class LoginTokenEntry extends BaseDBObject{

    public LoginTokenEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public LoginTokenEntry(ObjectId tokenId){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("ti",tokenId)
                .append("uid",null)
                .append("st", false);
        setBaseEntry(basicDBObject);
    }

    public void setStatus(boolean status){
        setSimpleValue("st",status);
    }

    public boolean getStatus(){
        return getSimpleBoolean("st");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setTokenId(ObjectId tokenId){
        setSimpleValue("ti",tokenId);
    }

    public ObjectId getTokenId(){
        return getSimpleObjecIDValue("ti");
    }
}
