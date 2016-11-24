package com.db.indicator;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.indicator.IndicatorTreeAppliedEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/11/14.
 */
public class IndicatorTreeAppliedDao extends BaseDao {

    /**
     * 添加评价
     * @param e
     * @return
     */
    public ObjectId addITreeAppliedEntry(IndicatorTreeAppliedEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INDICATOR_TREE_APPLIED, e.getBaseEntry());
        return e.getID();
    }

    public int getITreeAppliedCountByParam(ObjectId schoolId, int role, ObjectId userId, String name) {
        BasicDBObject query =new BasicDBObject("ir", DeleteState.NORMAL.getState());
        query.append("sid", schoolId);
        if (UserRole.isHeadmaster(role)||UserRole.isManager(role)) {

        }else{
            BasicDBList list =new BasicDBList();
            list.add(new BasicDBObject("crid", userId));
            list.add(new BasicDBObject("evids", userId));
            query.append(Constant.MONGO_OR,list);
        }
        if(StringUtil.isEmpty(name)){
            query.append("nm", MongoUtils.buildRegex(name));
        }
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_INDICATOR_TREE_APPLIED,query);
    }

    public List<IndicatorTreeAppliedEntry> getITreeAppliedListByParam(ObjectId schoolId, int role, ObjectId userId, String name, int skip, int limit) {
        BasicDBObject query =new BasicDBObject("ir", DeleteState.NORMAL.getState());
        query.append("sid", schoolId);
        if (UserRole.isHeadmaster(role)||UserRole.isManager(role)) {

        }else{
            BasicDBList list =new BasicDBList();
            list.add(new BasicDBObject("crid", userId));
            list.add(new BasicDBObject("evids", userId));
            query.append(Constant.MONGO_OR,list);
        }
        if(StringUtil.isEmpty(name)){
            query.append("nm", MongoUtils.buildRegex(name));
        }

        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDICATOR_TREE_APPLIED, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        List<IndicatorTreeAppliedEntry> entryList=new ArrayList<IndicatorTreeAppliedEntry>();
        for(DBObject dbObject:list){
            IndicatorTreeAppliedEntry entry=new IndicatorTreeAppliedEntry((BasicDBObject)dbObject);
            entryList.add(entry);
        }
        return entryList;
    }

    public IndicatorTreeAppliedEntry getITreeAppliedById(ObjectId id) {
        BasicDBObject query=new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_INDICATOR_TREE_APPLIED, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new IndicatorTreeAppliedEntry((BasicDBObject)dbo);
        }
        return null;

    }

    public void delITreeAppliedEntry(ObjectId id, int role, ObjectId userId) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        if(!UserRole.isManager(role)){
            query.append("crid", userId);
        }
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir",DeleteState.DELETED.getState()).append("upid", userId));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_INDICATOR_TREE_APPLIED,query, update);
    }


    public void delITreeAppliedEntry(ObjectId id, ObjectId userId) {
        DBObject query =new BasicDBObject(Constant.ID, id);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir",DeleteState.DELETED.getState()).append("upid", userId));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_INDICATOR_TREE_APPLIED,query, update);
    }

    public void updITreeAppliedEntry(IndicatorTreeAppliedEntry e) {
        DBObject query =new BasicDBObject(Constant.ID, e.getID());
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_INDICATOR_TREE_APPLIED, query, update);
    }
}
