package com.db.registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ScoreEntry;
import com.pojo.registration.GrowthRecordEntry;
import com.pojo.registration.QualityObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 成长档案Dao
 *
 * @author cxy
 *         2015-11-25 14:55:57
 */
public class GrowthRecordDao extends BaseDao {

    /**
     * 添加一条成长档案
     *
     * @param e
     * @return
     */
    public ObjectId addGrowthRecordEntry(GrowthRecordEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_GROWTH_RECORD, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除一条成长档案
     *
     * @param id
     */
    public void deleteGrowthRecordEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_GROWTH_RECORD, query);
    }

    /**
     * 根据学期和UserId查询一个特定的成长档案
     */
    public GrowthRecordEntry getGrowthRecordEntryByTermAndUserId(String termType, ObjectId userId) {
        DBObject query = new BasicDBObject("tt", termType).append("uid", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_GROWTH_RECORD, query, Constant.FIELDS);
        if (null != dbo) {
            return new GrowthRecordEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据ID更新一条成长档案
     */
    public void updateGoodPerformanceById(ObjectId id, String goodPerformance) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject().append("gp", goodPerformance));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROWTH_RECORD, query, updateValue);
    }

    /**
     * 根据ID更新一条成长档案
     */
    public void updateMasterCommentById(ObjectId id, String masterComment) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject().append("mc", masterComment));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROWTH_RECORD, query, updateValue);
    }

    /**
     * 根据ID更新一条成长档案
     */
    public void updateQualityEducationById(ObjectId id, List<QualityObject> qualityEducationList) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject().append("qes", MongoUtils.fetchDBObjectList(qualityEducationList)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROWTH_RECORD, query, updateValue);
    }

    /**
     * 去重查询有哪些学期有考试
     *
     * @param schoolId
     * @return
     */
    public BasicDBList distinctExamTerm(ObjectId schoolId) {
        DBObject cmd = new BasicDBObject("distinct", Constant.COLLECTION_EXAM).append("key", "schY")
                .append("query", new BasicDBObject("sId", schoolId).append("df", Constant.ZERO));
        return (BasicDBList) MongoFacroty.getAppDB().command(cmd).get("values");
    }

    /**
     * 去重查询有哪些学期有考试(为某个特定学生进行查询，用于详情页)
     *
     * @param schoolId
     * @return
     */
    public BasicDBList distinctExamTermForUniqueStudent(ObjectId schoolId, ObjectId studentId) {
        DBObject cmd = new BasicDBObject("distinct", Constant.COLLECTION_SCORE).append("key", "exId")
                .append("query", new BasicDBObject("stuId", studentId));
        return (BasicDBList) MongoFacroty.getAppDB().command(cmd).get("values");
    }

    /**
     * 根据传入的考试ID集合去重查询未被删除的考试学期List
     *
     * @param schoolId
     * @return
     */
    public BasicDBList distinctExamTermFromExamIdCollection(Collection<ObjectId> examIds) {
        DBObject cmd = new BasicDBObject("distinct", Constant.COLLECTION_EXAM).append("key", "schY")
                .append("query", new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, examIds)).append("df", Constant.ZERO));
        return (BasicDBList) MongoFacroty.getAppDB().command(cmd).get("values");
    }

    /**
     * 通过schoolId和学期查询该学期所有考试
     *
     * @param schoolId
     */
    public List<ExamEntry> queryExamBySchoolIdAndTerm(ObjectId schoolId, ObjectId classId, String term) {
        List<ExamEntry> retList = new ArrayList<ExamEntry>();
        DBObject query = new BasicDBObject("sId", schoolId).append("schY", term).append("cList", classId).append("df", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("ed", Constant.ASC);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, Constant.FIELDS, orderBy);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new ExamEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 查询所给学期有哪些该同学参加的考试(为某个特定学生进行查询，用于详情页)
     *
     * @param schoolId
     */
    public List<ExamEntry> findExamListByTermForUniqueStudent(Collection<ObjectId> examIds, String term) {
        List<ExamEntry> retList = new ArrayList<ExamEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, examIds))
                .append("schY", term).append("df", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("ed", Constant.ASC);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, Constant.FIELDS, orderBy);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new ExamEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 去重查询有哪些学期有考试
     *
     * @param schoolId
     * @return
     */
    public BasicDBList distinctGrowthTermForUniqueStudent(ObjectId studentId) {
        DBObject cmd = new BasicDBObject("distinct", Constant.COLLECTION_GROWTH_RECORD).append("key", "tt")
                .append("query", new BasicDBObject("uid", studentId));
        return (BasicDBList) MongoFacroty.getAppDB().command(cmd).get("values");
    }

    /**
     * 获取区域联考考试信息
     *
     * @param examId
     * @param stydentId
     * @return
     */
    public ScoreEntry getScoreEntryForUniqueStudent(ObjectId examId, ObjectId studentId) {
        DBObject query = new BasicDBObject().append("exId", examId).append("stuId", studentId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, Constant.FIELDS);
        if (null != dbo) {
            return new ScoreEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 删除一条成长档案
     *
     * @param id
     */
    public void removeGrowthRecordBySchoolId(ObjectId schoolId, String termType) {
        DBObject query = new BasicDBObject("scid", schoolId).append("tt", termType);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_GROWTH_RECORD, query);
    }

}
