package com.db.schoolsecurity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.schoolsecurity.SchoolSecurityEntry;
import com.pojo.utils.DeleteState;
import com.pojo.utils.HandleState;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/6/18.
 */
public class SchoolSecurityDao extends BaseDao {

    /**
     * 添加校园安全
     *
     * @param e
     * @return
     */
    public ObjectId addSchoolSecurity(SchoolSecurityEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除校园安全
     *
     * @param id
     * @param operateUserId
     * @param operateTime
     */
    public void deleteSchoolSecurity(ObjectId id, ObjectId operateUserId, long operateTime) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("st", DeleteState.DELETED.getState())
                        .append("oui", operateUserId)
                        .append("ot", operateTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, query, update);
    }

    /**
     * 批量删除校园安全
     *
     * @param ids
     * @param operateUserId
     * @param operateTime
     */
    public void batchDeleteSchoolSecurity(List<ObjectId> ids, ObjectId operateUserId, long operateTime) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("st", DeleteState.DELETED.getState())
                        .append("oui", operateUserId)
                        .append("ot", operateTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, query, update);
    }


    /**
     * 处理校园安全
     *
     * @param id
     * @param operateUserId
     * @param operateTime
     */
    public void handleSchoolSecurity(ObjectId id, ObjectId operateUserId, long operateTime) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ih", HandleState.TREATED.getState())
                        .append("oui", operateUserId)
                        .append("ot", operateTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, query, update);
    }


    /**
     * 获取数量
     *
     * @param handleState
     * @param schoolId
     * @return
     */
    public int getSchoolSecurityCount(int handleState, String schoolId) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        if (handleState != 2) {
            query.append("ih", handleState);
        }
        query.append("si", new ObjectId(schoolId));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, query);
    }

    /**
     * 查询
     *
     * @param handleState 处理状态
     * @param userId      用户id
     * @param state
     * @param schoolId
     * @param skip        用于分页；大于0时生效
     * @param limit       用于分页；大于0时生效
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     */
    public List<SchoolSecurityEntry> getSchoolSecurityEntryList(int handleState, ObjectId userId, DeleteState state, String schoolId, int skip, int limit) throws ResultTooManyException {
        List<SchoolSecurityEntry> retList = new ArrayList<SchoolSecurityEntry>();
        BasicDBObject query = new BasicDBObject("st", state.getState());
        if (handleState == 3) {
            query.append("ui", userId);
        } else if (handleState != 2) {
            query.append("ih", handleState);
        }
        query.append("si", new ObjectId(schoolId));
        if (query.isEmpty())
            throw new ResultTooManyException();

        BasicDBObject sort = new BasicDBObject("pbt", Constant.DESC);
        List<DBObject> list = new ArrayList<DBObject>();
        if (skip >= 0 && limit > 0) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, query, Constant.FIELDS, sort, skip, limit);
        } else {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, query, Constant.FIELDS, sort);
        }

        for (DBObject dbo : list) {
            retList.add(new SchoolSecurityEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 查询单条校园安全信息
     *
     * @param id
     * @param fields
     * @return
     */
    public SchoolSecurityEntry getOneSchoolSecurityInfo(ObjectId id, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("st", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_SECURITY, query, fields);
        if (null != dbo) {
            return new SchoolSecurityEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
