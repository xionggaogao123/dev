package com.db.Competition;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.competition.CompetitionItemDetailEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/2/29.
 */
public class CompetitionItemDetailDao extends BaseDao {

    /**
     * 添加一条评比明细信息
     *
     * @param e
     * @return
     */
    public ObjectId addCompetitionItemDetailEntry(CompetitionItemDetailEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_DETAIL, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 增加评比项目明细
     *
     * @param list
     * @return
     */
    public void addCompetitionItemDetailEntryList(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_DETAIL, list);
    }

    /**
     * 修改评比项目明细
     *
     * @param e
     * @return
     */
    public void updCompetitionItemDetailEntry(CompetitionItemDetailEntry e) {
        BasicDBObject query = new BasicDBObject(Constant.ID, e.getID());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_DETAIL, query, update);
    }

    /**
     * 删除评比项目明细
     *
     * @param ids
     */
    public void removeCompetitionItemDetailEntryByIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_DETAIL, query);
    }

    /**
     * 删除评比项目明细
     *
     * @param id
     */
    public void removeCompetitionItemDetailEntryById(ObjectId id) {
        if (null != id) {
            BasicDBObject query = new BasicDBObject(Constant.ID, id);
            remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_DETAIL, query);
        }
    }

    /**
     * 删除评比项目明细
     *
     * @param itemId
     */
    public void removeCompetitionItemDetailEntryByItemId(ObjectId itemId) {
        if (null != itemId) {
            BasicDBObject query = new BasicDBObject("itid", itemId);
            remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION_DETAIL, query);
        }
    }

    /**
     * 查询评比项目明细
     *
     * @param itemId
     */
    public List<CompetitionItemDetailEntry> getCompetitionItemDetailsByItemId(ObjectId itemId) {
        List<CompetitionItemDetailEntry> retList = new ArrayList<CompetitionItemDetailEntry>();
        if (null != itemId) {
            BasicDBObject dbo = new BasicDBObject();
            dbo.append("itid", itemId);
            List<DBObject> list = find(MongoFacroty.getAppDB(),
                    Constant.COLLECTION_COMPETITION_DETAIL, dbo, Constant.FIELDS,
                    Constant.MONGO_SORTBY_DESC);
            if (null != list && !list.isEmpty()) {
                CompetitionItemDetailEntry e = null;
                for (DBObject dbo1 : list) {
                    e = new CompetitionItemDetailEntry((BasicDBObject) dbo1);
                    retList.add(e);
                }
            }
        }
        return retList;
    }

    /**
     * 查询评比项目明细
     *
     * @param comId
     */
    public List<CompetitionItemDetailEntry> getCompetitionItemDetailsByComId(ObjectId comId) {
        List<CompetitionItemDetailEntry> retList = new ArrayList<CompetitionItemDetailEntry>();
        if (null != comId) {
            BasicDBObject dbo = new BasicDBObject();
            dbo.append("coid", comId);
            List<DBObject> list = find(MongoFacroty.getAppDB(),
                    Constant.COLLECTION_COMPETITION_DETAIL, dbo, Constant.FIELDS,
                    Constant.MONGO_SORTBY_DESC);
            if (null != list && !list.isEmpty()) {
                CompetitionItemDetailEntry e = null;
                for (DBObject dbo1 : list) {
                    e = new CompetitionItemDetailEntry((BasicDBObject) dbo1);
                    retList.add(e);
                }
            }
        }
        return retList;
    }
}
