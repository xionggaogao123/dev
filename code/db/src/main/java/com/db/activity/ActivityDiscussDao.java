package com.db.activity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.activity.ActivityDiscuss;
import com.pojo.activity.ActivityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by yan on 2015/3/9.
 */
public class ActivityDiscussDao extends BaseDao {
    public void insertDiscuss(ObjectId actId, ActivityDiscuss ad) {
        BasicDBObject query = new BasicDBObject(Constant.ID, actId);
        ad.setId(new ObjectId());
        BasicDBObject update = new BasicDBObject("diss", ad.getBaseEntry());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, updateValue);
    }

    public List<ActivityDiscuss> findDiscussByActId(ObjectId actId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, actId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) dbObject);
        return activityEntry.getActDiscusses();
    }
}
