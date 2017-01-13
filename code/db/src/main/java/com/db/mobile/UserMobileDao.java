package com.db.mobile;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.mobile.UserMobileEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by jerry on 2017/1/4.
 */
public class UserMobileDao extends BaseDao {

    public void save(UserMobileEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_MOBILE, entry.getBaseEntry());
    }

    public UserMobileEntry findByMobile(String mobile) {
        BasicDBObject query = new BasicDBObject("mob", mobile);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_MOBILE, query);
        return dbo == null ? null : new UserMobileEntry(dbo);
    }

    public void pushUserId(String mobile, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("mob", mobile);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("uids", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_MOBILE, query, update);
    }

    public void pullUserId(String mobile, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("mob", mobile);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("uids", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_MOBILE, query, update);
    }

    public boolean isExist(String mobile) {
        BasicDBObject query = new BasicDBObject("mob", mobile);
        return findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_MOBILE, query) != null;
    }

    public boolean isCanBind(String mobile) {
        UserMobileEntry userMobileEntry = findByMobile(mobile);
        return userMobileEntry == null || userMobileEntry.getUserIds().size() < 3;
    }

    public void clearPhone(String mobile) {
        BasicDBObject query = new BasicDBObject("mob", mobile);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_MOBILE, query);
    }
}
