package com.db.Competition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.pojo.pet.PetInfo;
import com.pojo.pet.UserPetEntry;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.competition.CompetitionBatch;
import com.pojo.competition.CompetitionEntry;
import com.pojo.competition.CompetitionItem;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 评比Dao
 *
 * @author cxy
 */
public class CompetitionDao extends BaseDao {

    /**
     * 添加一条评比信息
     *
     * @param e
     * @return
     */
    public ObjectId addCompetitionEntry(CompetitionEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据Id查询一个特定的评比信息
     *
     * @param id
     * @return
     */
    public CompetitionEntry getCompetitionEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, Constant.FIELDS);
        if (null != dbo) {
            return new CompetitionEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据条件查询一个特定的评比信息
     *
     * @param id
     * @return
     */
    public CompetitionEntry getCompetitionEntryByParam(ObjectId id, List<ObjectId> batchIds) {
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject(Constant.ID, id));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT,
                new BasicDBObject("tt", 1)
                        .append("cna", 1)
                        .append("cps", 1)
                        .append("cra", 1)
                        .append("cits", 1)
                        .append("cbas", 1)
                        .append("cd", 1)
                        .append("ir", 1)
                        .append("scid", 1));

        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$cbas");
        BasicDBObject searchDBO = new BasicDBObject();
        BasicDBList dblist = new BasicDBList();
        if (batchIds.size() == 1) {
            dblist.add(new BasicDBObject("cbas.bid", batchIds.get(0)));
        } else if (batchIds.size() > 1) {
            dblist.add(new BasicDBObject("cbas.bid", new BasicDBObject(Constant.MONGO_GTE, batchIds.get(0))));
            dblist.add(new BasicDBObject("cbas.bid", new BasicDBObject(Constant.MONGO_LTE, batchIds.get(1))));
        }
        if (dblist.size() > 0) {
            searchDBO.append(Constant.MONGO_AND, dblist);
        }
        DBObject matchDBO1 = new BasicDBObject(Constant.MONGO_MATCH, searchDBO);
        List<CompetitionBatch> batchList = new ArrayList<CompetitionBatch>();
        CompetitionEntry competitionEntry = null;
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, matchDBO, projectDBO, unbindDBO, matchDBO1);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject compObject;
            BasicDBObject compBatch;
            int count = 0;
            while (iter.hasNext()) {
                compObject = (BasicDBObject) iter.next();
                if (count == 0) {
                    competitionEntry = new CompetitionEntry(compObject);
                    count++;
                }
                compBatch = (BasicDBObject) compObject.get("cbas");
                batchList.add(new CompetitionBatch(compBatch));
            }
            if (competitionEntry != null) {
                competitionEntry.setCompetitionBatches(batchList);
            }
        } catch (Exception e) {
        }
        return competitionEntry;
    }

    /**
     * 删除一条评比信息
     *
     * @param id
     */
    public void deleteCompetition(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, updateValue);
    }

    /**
     * 根据ID更新一条评比信息的评比项目
     */
    public void updateCompetitionItems(ObjectId id, List<CompetitionItem> competitionItems) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("cits", MongoUtils.convert(MongoUtils.fetchDBObjectList(competitionItems))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, updateValue);

    }

    /**
     * 根据ID更新一条评比信息的评比批次
     */
    public void updateCompetitionBatches(ObjectId id, List<CompetitionBatch> competitionBatches) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("cbas", MongoUtils.convert(MongoUtils.fetchDBObjectList(competitionBatches))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, updateValue);
    }

    /**
     * 根据ID更新一条评比的其他信息
     *
     * @param id
     * @param competitionBatches
     */
    public void updateCompetition(ObjectId id, String competitionName, String competitionPostscript,
                                  List<ObjectId> competitionRange) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("cna", competitionName)
                        .append("cps", competitionPostscript)
                        .append("cra", MongoUtils.convert(competitionRange)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, updateValue);
    }

    /**
     * 根据学校ID和学期查询所有评比信息
     *
     * @param id
     * @return
     */
    public List<CompetitionEntry> getCompetitionsBySchoolIdAndTermType(ObjectId schoolId, String termType) {
        BasicDBObject query = new BasicDBObject();
        query.append("ir", Constant.ZERO)
                .append("scid", schoolId);
        if (termType != null) {
            query.append("tt", termType);
        }
        DBObject orderBy = new BasicDBObject("cd", Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, Constant.FIELDS, orderBy);
        List<CompetitionEntry> resultList = new ArrayList<CompetitionEntry>();
        for (DBObject dbObject : dbObjects) {
            CompetitionEntry competitionEntry = new CompetitionEntry((BasicDBObject) dbObject);
            resultList.add(competitionEntry);
        }
        return resultList;
    }

    /**
     * 修改某一个评比中某一个评比项目的信息
     */
    public void updateCompetitionItemForOne(ObjectId competitionId, ObjectId itemId, String itemName, String itemPostscript, int itemFullScore) {
        DBObject query = new BasicDBObject(Constant.ID, competitionId).append("cits.itid", itemId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cits.$.itna", itemName)
                .append("cits.$.itps", itemPostscript)
                .append("cits.$.itfs", itemFullScore));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, updateValue);
    }

    /**
     * 为某一个评比新增一个评比项目
     */
    public void addCompetitionItemforCompetition(ObjectId competitionId, CompetitionItem item) {
        DBObject query = new BasicDBObject(Constant.ID, competitionId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("cits", item.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, updateValue);
    }

    /**
     * 删除一个评比的一个评比项目
     */
    public void deleteCompetitionItemForCompetition(ObjectId competitionId, CompetitionItem item) {
        DBObject query = new BasicDBObject(Constant.ID, competitionId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cits", item.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMPETITION, query, updateValue);
    }
}
