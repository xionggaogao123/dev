package com.db.reward;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reward.RewardEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 奖惩Dao
 *
 * @author cxy
 */
public class RewardDao extends BaseDao {

    /**
     * 添加一条奖惩信息
     *
     * @param e
     * @return
     */
    public ObjectId addRewardEntry(RewardEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REWARD, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据Id查询一个特定的奖惩信息
     *
     * @param id
     * @return
     */
    public RewardEntry getRewardEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REWARD, query, Constant.FIELDS);
        if (null != dbo) {
            return new RewardEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据传的参数进行查询，
     *
     * @param gradeId     default(null)
     * @param classId     default(null)
     * @param rewardType  default(ALL)
     * @param rewardGrade default(ALL)
     * @param studentName default(ALL)
     * @return
     */
    public List<RewardEntry> queryRewardsByfields(ObjectId gradeId, ObjectId classId, String rewardType, String rewardGrade, String studentName, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject();
        if (gradeId != null) {
            query.append("gid", gradeId);
        }
        if (classId != null) {
            query.append("cid", classId);
        }
        if (!("ALL".equals(rewardType))) {
            query.append("rt", rewardType);
        }
        if (!("ALL".equals(rewardGrade))) {
            query.append("rg", rewardGrade);
        }
        if (!("ALL".equals(studentName))) {
            query.append("sna", studentName);
        }
        query.append("scid", schoolId);
        query.append("ir", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("gna", 1).append("cna", 1);
//		orderBy.append( new BasicDBObject("gid",Constant.DESC).append("cid",Constant.DESC));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REWARD, query, Constant.FIELDS, orderBy);
        List<RewardEntry> resultList = new ArrayList<RewardEntry>();
        for (DBObject dbObject : dbObjects) {
            RewardEntry rewardEntry = new RewardEntry((BasicDBObject) dbObject);
            resultList.add(rewardEntry);
        }
        return resultList;
    }

    /**
     * 根据ID更新一条奖惩信息
     *
     * @param id
     * @param rewardType
     * @param rewardGrade
     * @param rewardDate
     * @param rewardContent
     * @param departments
     * @param classes
     */
    public void updateReward(ObjectId id, String rewardType, String rewardGrade, long rewardDate,
                             String rewardContent, List<ObjectId> departments, List<ObjectId> classes) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("rt", rewardType)
                        .append("rg", rewardGrade)
                        .append("rd", rewardDate)
                        .append("rc", rewardContent)
                        .append("prt", MongoUtils.convert(departments))
                        .append("prs", MongoUtils.convert(classes)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REWARD, query, updateValue);

    }

    /**
     * 删除一条
     *
     * @param id
     */
    public void deleteReward(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REWARD, query, updateValue);
    }

    /**
     * 查询某个学生的所有奖惩记录
     *
     * @param studentId 学生ID,String
     * @return
     */
    public List<RewardEntry> queryRewardsByUserId(String studentId) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", studentId);
        query.append("ir", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("rd", 1);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REWARD, query, Constant.FIELDS, orderBy);
        List<RewardEntry> resultList = new ArrayList<RewardEntry>();
        for (DBObject dbObject : dbObjects) {
            RewardEntry rewardEntry = new RewardEntry((BasicDBObject) dbObject);
            resultList.add(rewardEntry);
        }
        return resultList;
    }
}
