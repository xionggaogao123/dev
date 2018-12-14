package com.db.instantmessage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.instantmessage.RedDotEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/10/25.
 */
public class RedDotDao extends BaseDao {
    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, list);
    }

    //删除
    public void updateEntry(ObjectId id,int number){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("num",number));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query,updateValue);
    }

    //单个查询 otner类型
    public RedDotEntry getEntryByUserId(ObjectId userId,int type){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",userId);
        query.append("typ",type);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query, Constant.FIELDS);
        if (obj != null) {
            return new RedDotEntry((BasicDBObject) obj);
        }
        return null;
    }
    public List<RedDotEntry> getAllEntry(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_RED_DOT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<RedDotEntry> entryList = new ArrayList<RedDotEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new RedDotEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //单个查询 same类型
    public RedDotEntry getOtherEntryByUserId(ObjectId userId,long dataTime,int type){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",userId);
        query.append("typ",type);
        query.append("dtm",dataTime);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query, Constant.FIELDS);
        if (obj != null) {
            return new RedDotEntry((BasicDBObject) obj);
        }
        return null;
    }
    //批量查询 otner类型
    public List<ObjectId> getOtherRedDotEntryByList(List<ObjectId> userIds,long time,int type) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
                .append("typ", type)
                .append("dtm",time)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_RED_DOT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new RedDotEntry((BasicDBObject) obj).getUserId());
            }
        }
        return entryList;
    }
    //批量查询 same类型
    public List<ObjectId> getRedDotEntryByList(List<ObjectId> userIds,int type) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
                .append("typ", type)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_RED_DOT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new RedDotEntry((BasicDBObject) obj).getUserId());
            }
        }
        return entryList;
    }

    //批量修改 otner类型
    public void updateEntry1(List<ObjectId> userIds,long time,int type){
        BasicDBObject query = new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        query.append("typ", type);
        query.append("dtm", time);
        query .append("isr", 0); // 未删除
        BasicDBObject updateValue = new BasicDBObject() .append(Constant.MONGO_INC, new BasicDBObject("num", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query, updateValue);
    }

    //批量修改 same类型
    public void updateEntry2(List<ObjectId> userIds,int type){
        BasicDBObject query = new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        query.append("typ", type);
        query .append("isr", 0); // 未删除
        BasicDBObject updateValue =  new BasicDBObject() .append(Constant.MONGO_INC, new BasicDBObject("num", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query, updateValue);
    }
    //清空特定记录 otner类型
    public void cleanEntry1(ObjectId userId,long dataTime,int type){
        BasicDBObject query = new BasicDBObject("uid",userId);
        query.append("typ", type);
        query.append("dtm", dataTime);
        query .append("isr", 0); // 未删除
        BasicDBObject updateValue = new BasicDBObject().append(Constant.MONGO_SET, new BasicDBObject("num", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query, updateValue);
    }

    //清空特定记录 same类型
    public void cleanEntry2(ObjectId userId,int type){
        BasicDBObject query = new BasicDBObject("uid",userId);
        query.append("typ", type);
        query .append("isr", 0); // 未删除
        BasicDBObject updateValue = new BasicDBObject().append(Constant.MONGO_SET,new BasicDBObject("num", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query, updateValue);
    }

    //清空特定记录 same类型
    public void cleanIndexEntry(ObjectId userId,List<Integer> types){
        BasicDBObject query = new BasicDBObject("uid",userId);
        query.append("typ", new BasicDBObject(Constant.MONGO_IN,types));
        query .append("isr", 0); // 未删除
        BasicDBObject updateValue = new BasicDBObject().append(Constant.MONGO_SET,new BasicDBObject("num", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_RED_DOT, query, updateValue);
    }
}
