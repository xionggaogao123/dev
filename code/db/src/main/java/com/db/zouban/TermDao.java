package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.TermEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/11/5.
 */
public class TermDao extends BaseDao {
    /**
     * 添加教学周
     *
     * @param termEntry
     */
    public void addTerm(TermEntry termEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_TERMWEEK, termEntry.getBaseEntry());
    }

    /**
     * 获取教学周
     *
     * @param schoolId
     * @return
     */
    public TermEntry findTermEntry(ObjectId schoolId, String year) {
        BasicDBObject query = new BasicDBObject()
                .append("year", year)
                .append("sid", schoolId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_TERMWEEK, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TermEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 更新教学周
     *
     * @param termEntry
     */
    public void updateTermEntry(TermEntry termEntry) {
        BasicDBObject query = new BasicDBObject()
                .append("year", termEntry.getYear())
                .append("sid", termEntry.getSchoolId());
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject()
                        .append("fts", termEntry.getFirstTermStart())
                        .append("fte", termEntry.getFirstTermEnd())
                        .append("sts", termEntry.getSecondTermStart())
                        .append("ste", termEntry.getSecondTermEnd()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_TERMWEEK, query, updateValue);
    }

    /**
     * 判断是否是第一学期
     *
     * @param schoolId
     * @param data
     * @return
     */
    public TermEntry findFirstTermByDate(ObjectId schoolId, long data) {
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("fts", new BasicDBObject(Constant.MONGO_LTE, data))
                .append("fte", new BasicDBObject(Constant.MONGO_GTE, data));
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_TERMWEEK, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TermEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 判断是否是第二学期
     *
     * @param schoolId
     * @param data
     * @return
     */
    public TermEntry findSecondTermByDate(ObjectId schoolId, long data) {
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("sts", new BasicDBObject(Constant.MONGO_LTE, data))
                .append("ste", new BasicDBObject(Constant.MONGO_GTE, data));
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_TERMWEEK, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TermEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    //寻找处于两个学期之间的
    public TermEntry findDuringTermByDate(ObjectId schoolId, long data) {
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("fte", new BasicDBObject(Constant.MONGO_LTE, data))
                .append("sts", new BasicDBObject(Constant.MONGO_GTE, data));
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_TERMWEEK, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TermEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    //寻找比当前时间大的第一学期
    public List<TermEntry> findFirstTermGteNow(ObjectId schoolId, long data) {
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("fts", new BasicDBObject(Constant.MONGO_GTE, data));
        List<DBObject> dbObjectLsit = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_TERMWEEK, query, Constant.FIELDS);
        List<TermEntry> termEntries = new ArrayList<TermEntry>();
        if (dbObjectLsit != null && !dbObjectLsit.isEmpty()) {
            for (DBObject dbObject : dbObjectLsit) {
                termEntries.add(new TermEntry((BasicDBObject) dbObject));
            }
        }
        return termEntries;
    }
}
