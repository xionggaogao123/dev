package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.MessageDateEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-11-23.
 */
public class MessageDateDao extends BaseDao {

    public void addEntry(MessageDateEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MESSAGE_DATE, entry.getBaseEntry());
    }

    public MessageDateEntry getEntry(ObjectId userId,int type){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("typ",type);
        query.append("uid",userId);
        DBObject dbObject= findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_MESSAGE_DATE,query);
        if(null!=dbObject){
            return new MessageDateEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }

    }

}
