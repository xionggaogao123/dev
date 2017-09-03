package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppCommentEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/8/25.
 */
public class AppCommentDao extends BaseDao {
    //添加
    public String addEntry(AppCommentEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, entry.getBaseEntry());
        return entry.getID().toString();
    }

    public AppCommentEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, query, Constant.FIELDS);
        if (obj != null) {
            return new AppCommentEntry((BasicDBObject) obj);
        }
        return null;
    }

    //老师当日作业列表查询
    public List<AppCommentEntry> getEntryListByUserId(ObjectId userId,long dateTime) {
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("dtm",dateTime)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //老师月作业列表查询
    public List<AppCommentEntry> selectResultList(ObjectId userId,int month) {
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("mon",month)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //老师日作业列表查询
    public List<AppCommentEntry> selectDateList(ObjectId userId,long dateTime) {
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("dtm",dateTime)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //家长日作业列表查询
    public List<AppCommentEntry> selectDateList2(List<ObjectId> userIds,long dateTime) {
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("dtm",dateTime)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
