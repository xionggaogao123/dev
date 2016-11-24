package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FPostEntry;
import com.pojo.forum.FRecordDTO;
import com.pojo.forum.FRecordEntry;
import com.pojo.forum.FReplyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/21.
 */
public class FRecordDao extends BaseDao {


    /**
     * 添加回帖
     *
     * @param e
     * @return
     */
    public ObjectId addFRecord(FRecordEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_RECORD, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询数据（根据personId来查询）
     *
     * @param fields
     * @return
     */
    public List<FRecordEntry> getFRecordEntries(DBObject fields, DBObject sort, ObjectId personId) {
        List<FRecordEntry> retList = new ArrayList<FRecordEntry>();

        BasicDBObject query = new BasicDBObject();
        if (personId != null) {
            query.append("uid", personId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_RECORD, query, fields, sort);

        for (DBObject dbObject : dbObjectList) {
            retList.add(new FRecordEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

    /**
     * 查帖子数量
     *
     * @return
     */
    public int getFRecordEntriesCount(ObjectId personId) {

        BasicDBObject query = new BasicDBObject();

        if (personId != null) {
            query.append("uid", personId);
        }
        query.append("ls", 0);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_RECORD, query);

        return count;
    }

    /**
     * 更新删除记录
     */
    public void updateDelete() {
        DBObject query = new BasicDBObject("log", 1);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ls", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_RECORD, query, updateValue);
    }

    /**
     * 更新回复记录
     */
    public void updateReply(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ls", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_RECORD, query, updateValue);
    }

    /**
     * 回帖详情
     *
     * @param id
     * @return
     */
    public FRecordEntry getFRecordEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_RECORD, query, Constant.FIELDS);
        if (null != dbo) {
            return new FRecordEntry((BasicDBObject) dbo);
        }
        return null;
    }
}

