package com.db.teacherevaluation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teacherevaluation.SettingEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/4/21.
 */
public class SettingDao extends BaseDao {

    private static final String COLLECTION_NAME = Constant.COLLECTION_TE_SETTING;

    public ObjectId addSetting(SettingEntry entry) {
        save(MongoFacroty.getAppDB(), COLLECTION_NAME, entry.getBaseEntry());
        return entry.getID();
    }

    public SettingEntry getSettingEntry(ObjectId schoolId, String year) {
        DBObject query = new BasicDBObject("si", schoolId).append("y", year);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        if (dbObject != null) {
            return new SettingEntry((BasicDBObject) dbObject);
        }
        return null;
    }


}
