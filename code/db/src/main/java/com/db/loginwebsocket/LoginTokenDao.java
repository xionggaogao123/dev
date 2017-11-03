package com.db.loginwebsocket;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.loginwebsocket.LoginTokenEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/2.
 */
public class LoginTokenDao extends BaseDao{

    public void saveEntry(LoginTokenEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TOKEN_USER_LOGIN,entry.getBaseEntry());
    }

    public void updateTokenStatus(ObjectId tokenId){
        BasicDBObject query=new BasicDBObject()
                .append("ti",tokenId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st", false));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TOKEN_USER_LOGIN,query,updateValue);
    }

    public LoginTokenEntry getEntry(){
        BasicDBObject query=new BasicDBObject()
                .append("st", false);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TOKEN_USER_LOGIN,query,Constant.FIELDS);
        if(null!=dbObject){
            return new LoginTokenEntry(dbObject);
        }else{
            return null;
        }
    }

    public LoginTokenEntry getEntry(ObjectId tokenId){
        BasicDBObject query=new BasicDBObject()
                .append("ti",tokenId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TOKEN_USER_LOGIN,query,Constant.FIELDS);
        if(null!=dbObject){
            return new LoginTokenEntry(dbObject);
        }else{
            return null;
        }
    }
}
