package com.fulaan.user.dao;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.fulaan.user.model.ThirdLoginEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class ThirdLoginDao extends BaseDao {


    /**
     * 保存第三方登录实体
     *
     * @param e
     * @return
     */
    public ObjectId addThidLoginEntry(ThirdLoginEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_THIRD_LOGIN_NAME, e.getBaseEntry());
        return e.getID();
    }

    public ThirdLoginEntry getThirdLoginEntryByMap(Map<String, Object> map) {
        Iterator iterator = map.keySet().iterator();
        BasicDBObject query = new BasicDBObject();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            query.append(key, map.get(key));
        }
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_THIRD_LOGIN_NAME, query, null);
        ThirdLoginEntry thirdLoginEntry = null;
        if (null != dbo) {
            thirdLoginEntry = new ThirdLoginEntry((BasicDBObject) dbo);
        }
        return thirdLoginEntry;
    }

    public boolean isBindQQ(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid",userId).append("type",2);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_THIRD_LOGIN_NAME,query) == 1;
    }

    public boolean isBindWechat(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid",userId).append("type",1);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_THIRD_LOGIN_NAME,query) == 1;
    }



    /**
     * 获取第三方用户实体
     *
     * @param map 键值对
     * @return UserEntry
     */
    public UserEntry getEntryByMap(Map<String, Object> map) {

        Iterator iterator = map.keySet().iterator();
        BasicDBObject query = new BasicDBObject();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            query.append(key, map.get(key));
        }
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_THIRD_LOGIN_NAME, query, null);
        if (null != dbo) {
            ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry((BasicDBObject) dbo);
            ObjectId uid = thirdLoginEntry.getUid();
            BasicDBObject queryUserEntry = new BasicDBObject("_id", uid);
            DBObject dboUserEntry = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, queryUserEntry, null);
            if (null != dboUserEntry) {
                return new UserEntry(dboUserEntry);
            }
            return null;
        }
        return null;
    }

}
