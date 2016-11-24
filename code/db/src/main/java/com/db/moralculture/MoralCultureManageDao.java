package com.db.moralculture;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.moralculture.MoralCultureManageEntry;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/7/3.
 */
public class MoralCultureManageDao extends BaseDao {

    /**
     * 添加德育项目
     *
     * @param e
     * @return
     */
    public ObjectId addMoralCultureProject(MoralCultureManageEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_MANAGE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除德育项目
     *
     * @param id
     * @param userId
     * @param time
     * @return
     */
    public void delMoralCultureProject(ObjectId id, ObjectId userId, long time) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("st", DeleteState.DELETED.getState())
                        .append("ub", userId)
                        .append("ut", time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_MANAGE, query, update);
    }

    /**
     * 修改德育项目
     *
     * @param id
     * @param moralCultureName
     * @param userId
     * @param time
     * @return
     */
    public void updMoralCultureProject(ObjectId id, String moralCultureName, ObjectId userId, long time) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("nm", moralCultureName)
                        .append("ub", userId)
                        .append("ut", time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_MANAGE, query, update);
    }

    /**
     * 德育项目列表
     *
     * @param schoolId
     * @param state
     * @return
     */
    public List<MoralCultureManageEntry> selMoralCultureProjectList(ObjectId schoolId, DeleteState state) {
        List<MoralCultureManageEntry> reList = new ArrayList<MoralCultureManageEntry>();
        BasicDBObject query = new BasicDBObject("st", state.getState());
        query.append("si", schoolId);
        BasicDBObject sort = new BasicDBObject("ct", Constant.ASC);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_MANAGE, query, Constant.FIELDS, sort);
        for (DBObject dbo : list) {
            reList.add(new MoralCultureManageEntry((BasicDBObject) dbo));
        }
        return reList;
    }

    /**
     * 查询单条德育项目
     *
     * @param id
     * @return
     */
    public MoralCultureManageEntry selMoralCultureProjectInfo(String id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("st", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_MANAGE, query, Constant.FIELDS);
        if (null != dbo) {
            return new MoralCultureManageEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
