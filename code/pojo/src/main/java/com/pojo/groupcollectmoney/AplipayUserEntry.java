package com.pojo.groupcollectmoney;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2018/1/17.
 */
public class AplipayUserEntry extends BaseDBObject{

    public AplipayUserEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public AplipayUserEntry(ObjectId userId,
                            String rsaPrivateUserId){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("rpu",rsaPrivateUserId)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public String getRsaPrivateUserId(){
        return getSimpleStringValue("rpu");
    }

    public void setRsaPrivateUserId(String rsaPrivateUserId){
        setSimpleValue("rpu",rsaPrivateUserId);
    }


    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
}
