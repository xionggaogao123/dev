package com.db.crontab;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.pojo.crontab.CrontabEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/3/18.
 */
public class CrontabDao extends BaseDao {

    public ObjectId add(CrontabEntry crontabEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CRONTAB, crontabEntry.getBaseEntry());
        return crontabEntry.getID();
    }

    public CrontabEntry getCrontabEntry(String name){
        DBObject query = new BasicDBObject("nm", name);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CRONTAB, query, Constant.FIELDS);
        if(dbObject != null) {
            return new CrontabEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    public CrontabEntry update(String name, int version){
        DBCollection db = MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_CRONTAB);
        DBObject query = new BasicDBObject("nm", name);
        DBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ver", version));
        DBObject oldData = db.findAndModify(query, update);
        return new CrontabEntry((BasicDBObject)oldData);
    }
}
