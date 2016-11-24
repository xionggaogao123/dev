package com.db.businessactivity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.businessactivity.FieryActivityEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guojing on 2015/7/30.
 */
public class FieryActivityDao extends BaseDao {

    /**
     * 添加火热活动
     *
     * @param e
     * @return
     */
    public ObjectId addFieryActivity(FieryActivityEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 得到总数，用于分页
     *
     * @return
     */
    public int getFieryActivityCount(ObjectId eduId, int role, List<Integer> roles) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        if (!UserRole.isSysManager(role)) {
            query.append("te", 0);
            query.append("rl", new BasicDBObject(Constant.MONGO_IN, roles));
            if (eduId != null) {
                BasicDBObject query1 = new BasicDBObject();
                BasicDBList list = new BasicDBList();
                BasicDBList list1 = new BasicDBList();
                list1.add(new BasicDBObject("eis", eduId));
                list1.add(new BasicDBObject("ia", 0));
                //list1.add(new BasicDBObject("if", 0));
                query1.append(Constant.MONGO_AND, list1);
                list.add(query1);
                list.add(new BasicDBObject("ia", 1));
                query.append(Constant.MONGO_OR, list);
            } else {
                query.append("ia", 1);
            }
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query);
    }

    /**
     * 得到火热活动集合，用于分页
     *
     * @return
     */
    public List<FieryActivityEntry> getFieryActivitys(ObjectId eduId, int role, List<Integer> roles, int skip, int limit) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        BasicDBObject sort = new BasicDBObject();
        if (!UserRole.isSysManager(role)) {
            query.append("te", 0);
            query.append("rl", new BasicDBObject(Constant.MONGO_IN, roles));
            if (eduId != null) {
                BasicDBObject query1 = new BasicDBObject();
                BasicDBList list = new BasicDBList();
                BasicDBList list1 = new BasicDBList();
                list1.add(new BasicDBObject("eis", eduId));
                list1.add(new BasicDBObject("ia", 0));
                //list1.add(new BasicDBObject("if", 0));
                query1.append(Constant.MONGO_AND, list1);
                list.add(query1);
                list.add(new BasicDBObject("ia", 1));
                query.append(Constant.MONGO_OR, list);
                sort.append("ia", Constant.ASC).append("ct", Constant.DESC).append(Constant.ID, Constant.DESC);
            } else {
                query.append("ia", 1);
                sort.append("ct", Constant.DESC).append(Constant.ID, Constant.DESC);
            }
        } else {
            sort.append("ct", Constant.DESC).append(Constant.ID, Constant.DESC);
        }
        List<FieryActivityEntry> retList = new ArrayList<FieryActivityEntry>();
        List<DBObject> dboList = new ArrayList<DBObject>();
        if (skip >= 0 && limit > 0) {
            dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, Constant.FIELDS, sort, skip, limit);
        } else {
            dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, Constant.FIELDS, sort);
        }
        if (null != dboList && !dboList.isEmpty()) {
            FieryActivityEntry e;
            for (DBObject dbobject : dboList) {
                e = new FieryActivityEntry((BasicDBObject) dbobject);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 火热活动详情
     *
     * @param id
     * @return
     */
    public FieryActivityEntry getFieryActivityEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new FieryActivityEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 删除火热活动
     *
     * @param id
     * @return
     */
    public void deleteFieryActivity(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("st", DeleteState.DELETED.getState()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, update);
    }

    /**
     * 定时开启火热活动
     *
     * @return
     */
    public void FieryActivityIsStart() {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        query.append("te", 1);
        BasicDBList dblist = new BasicDBList();
        dblist.add(new BasicDBObject("bt", new BasicDBObject(Constant.MONGO_GTE, 0)));
        dblist.add(new BasicDBObject("bt", new BasicDBObject(Constant.MONGO_LTE, new Date().getTime())));
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("te", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, updateValue);
    }

    /**
     * 定时结束火热活动
     *
     * @return
     */
    public void FieryActivityIsEnd() {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        query.append("if", 0);
        BasicDBList dblist = new BasicDBList();
        dblist.add(new BasicDBObject("et", new BasicDBObject(Constant.MONGO_GT, 0)));
        dblist.add(new BasicDBObject("et", new BasicDBObject(Constant.MONGO_LTE, new Date().getTime())));
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("if", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, updateValue);
    }

    public void editFieryActivity(ObjectId id, FieryActivityEntry e) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, update);
    }

    /**
     * 得到火热活动有广告图片的集合，用于分页
     *
     * @return
     */
    public List<FieryActivityEntry> getBusinessActivityImage(ObjectId eduId, List<Integer> roles, int skip, int limit) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        query.append("te", 0);
        query.append("rl", new BasicDBObject(Constant.MONGO_IN, roles));
        query.append("pif", new BasicDBObject(Constant.MONGO_NE, Constant.EMPTY));
        BasicDBObject sort = new BasicDBObject();
        if (eduId != null) {
            BasicDBObject query1 = new BasicDBObject();
            BasicDBList list = new BasicDBList();
            BasicDBList list1 = new BasicDBList();
            list1.add(new BasicDBObject("eis", eduId));
            list1.add(new BasicDBObject("ia", 0));
            list1.add(new BasicDBObject("if", 0));
            query1.append(Constant.MONGO_AND, list1);
            list.add(query1);
            list.add(new BasicDBObject("ia", 1));
            query.append(Constant.MONGO_OR, list);
            sort.append("ia", Constant.ASC).append("ct", Constant.DESC).append(Constant.ID, Constant.DESC);
        } else {
            query.append("ia", 1);
            sort.append("ct", Constant.DESC).append(Constant.ID, Constant.DESC);
        }
        List<FieryActivityEntry> retList = new ArrayList<FieryActivityEntry>();
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FIERY_ACTIVITY_NAME, query, Constant.FIELDS, sort, skip, limit);
        if (null != dboList && !dboList.isEmpty()) {
            FieryActivityEntry e;
            for (DBObject dbobject : dboList) {
                e = new FieryActivityEntry((BasicDBObject) dbobject);
                retList.add(e);
            }
        }
        return retList;
    }
}
