package com.db.indicator;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.indicator.InterestEvaluateEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/10/31.
 */
public class InterestEvaluateDao extends BaseDao {
    /**
     * 添加扩展课 学生评价
     * @param e
     * @return
     */
    public ObjectId addInterestEvaluateEntry(InterestEvaluateEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_EVALUATE_RESULT, e.getBaseEntry());
        return e.getID();
    }

    public void addInterestEvaluateEntryList(List<InterestEvaluateEntry> entryList) {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(entryList);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_EVALUATE_RESULT, dbObjects);
    }

    public List<InterestEvaluateEntry> findInterestEvaluateListByParam(ObjectId appliedId, ObjectId activityId, int termType) {
        BasicDBObject query=new BasicDBObject("aid",activityId);
        query.append("apid", appliedId);
        if(termType > 0){
            query.append("tty", termType);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT,query,Constant.FIELDS);
        List<InterestEvaluateEntry> entryList=new ArrayList<InterestEvaluateEntry>();
        for(DBObject dbObject:list){
            InterestEvaluateEntry entry=new InterestEvaluateEntry((BasicDBObject)dbObject);
            entryList.add(entry);
        }
        return entryList;
    }

    public List<InterestEvaluateEntry> findInterestEvaluateListByParam(ObjectId appliedId, ObjectId activityId, int termType, DBObject fields) {
        BasicDBObject query=new BasicDBObject("aid",activityId);
        query.append("apid", appliedId);
        if(termType > 0){
            query.append("tty", termType);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT,query, fields);
        List<InterestEvaluateEntry> entryList=new ArrayList<InterestEvaluateEntry>();
        for(DBObject dbObject:list){
            InterestEvaluateEntry entry=new InterestEvaluateEntry((BasicDBObject)dbObject);
            entryList.add(entry);
        }
        return entryList;
    }

    public void updInterestEvaluateEntryById(ObjectId id, InterestEvaluateEntry entry) {
        DBObject query =new BasicDBObject(Constant.ID, id);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(entry.getBaseEntry()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT,query, update);
    }

    public InterestEvaluateEntry getStudentEvaluateEntry(ObjectId appliedId, ObjectId activityId, ObjectId commonToId, int termType) {
        BasicDBObject query=new BasicDBObject("aid",activityId);
        query.append("apid", appliedId);
        if(termType > 0){
            query.append("tty", termType);
        }
        query.append("ctid", commonToId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new InterestEvaluateEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public InterestEvaluateEntry getStudentEvaluateEntryById(ObjectId id) {
        BasicDBObject query=new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new InterestEvaluateEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public InterestEvaluateEntry getStudentEvaluateEntryById(ObjectId id, DBObject fields) {
        BasicDBObject query=new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT, query, fields);
        if(null!=dbo)
        {
            return new InterestEvaluateEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public List<InterestEvaluateEntry> findInterestEvaluatePageListByParam(
            ObjectId appliedId,
            ObjectId activityId,
            int termType,
            String name,
            List<ObjectId> uids,
            int skip,
            int limit
    ) {
        BasicDBObject query=new BasicDBObject("aid",activityId);
        query.append("apid", appliedId);
        if(termType > 0){
            query.append("tty", termType);
        }
        if(!"".equals(name)){
            query.append("ctid", new BasicDBObject(Constant.MONGO_IN, uids));
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT,query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        List<InterestEvaluateEntry> entryList=new ArrayList<InterestEvaluateEntry>();
        for(DBObject dbObject:list){
            InterestEvaluateEntry entry=new InterestEvaluateEntry((BasicDBObject)dbObject);
            entryList.add(entry);
        }
        return entryList;
    }

    public int findInterestEvaluateCountByParam(ObjectId appliedId, ObjectId activityId, int termType, String name, List<ObjectId> uids) {
        BasicDBObject query=new BasicDBObject("aid",activityId);
        query.append("apid", appliedId);
        if(termType > 0){
            query.append("tty", termType);
        }
        if(!"".equals(name)){
            query.append("ctid", new BasicDBObject(Constant.MONGO_IN, uids));
        }
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_EVALUATE_RESULT, query);
    }
}
