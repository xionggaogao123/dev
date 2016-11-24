package com.db.examregional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ScoreEntry;
import com.pojo.exam.SubjectScoreEntry;
import com.pojo.examregional.ExamSummaryEntry;
import com.pojo.examregional.RegionalExamEntry;
import com.pojo.examregional.RegionalSchItem;
import com.pojo.examregional.SubjectDetails;
import com.pojo.school.ClassEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 区域联考
 *
 * @author lujiang
 */
public class RegionalExamDao extends BaseDao {

    /**
     * 添加区域联考信息(l)
     */
    public ObjectId save(RegionalExamEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REGIONAL_EXAM,
                entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 添加区域联考汇总信息(cxy)
     */
    public ObjectId saveExamSummary(ExamSummaryEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_SUMMARY,
                entry.getBaseEntry());
        return entry.getID();
    }


    /**
     * 加载详细信息
     *
     * @param id
     * @return
     */
    public List<ScoreEntry> load(final String id) {
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE,
                new BasicDBObject().append("exId", new ObjectId(id)), Constant.FIELDS);
        List<ScoreEntry> resultList = new ArrayList<ScoreEntry>();
        for (DBObject obj : dbObjectList) {
            resultList.add(new ScoreEntry((BasicDBObject) obj));
        }
        return resultList;
    }

    /**
     * 加载详细信息
     *
     * @param id
     * @return
     */
    public List<ScoreEntry> loadByPage(final String id, int skip, int size) {
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE,
                new BasicDBObject().append("exId", new ObjectId(id)), Constant.FIELDS, null, skip, size);
        List<ScoreEntry> resultList = new ArrayList<ScoreEntry>();
        for (DBObject obj : dbObjectList) {
            resultList.add(new ScoreEntry((BasicDBObject) obj));
        }
        return resultList;
    }

    /**
     * 获取区域联考学生总人数
     *
     * @param examId
     * @return
     */
    public int countScoreEntry(final ObjectId examId) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, new BasicDBObject("exId", examId));
    }

    /**
     * 获取区域联考考试信息
     *
     * @param examId
     * @param subjectId
     * @return
     */
    public ExamEntry getExamEntryInfo(ObjectId id) {
        DBObject query = new BasicDBObject().append(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, Constant.FIELDS);
        if (null != dbo) {
            return new ExamEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 查询教育局创建的一条区域联考信息
     *
     * @param id
     * @return
     */
    public RegionalExamEntry getRegionalExamEntryInfo(ObjectId id) {
        DBObject query = new BasicDBObject().append(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REGIONAL_EXAM, query, Constant.FIELDS);
        if (null != dbo) {
            return new RegionalExamEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 加载详细信息
     *
     * @param id
     * @return
     */
    public ScoreEntry loadPerformanceById(final String id) {
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE,
                new BasicDBObject().append(Constant.ID, new ObjectId(id)), Constant.FIELDS);
        return null != dbObject ? new ScoreEntry((BasicDBObject) dbObject) : null;
    }

    /**
     * 根据ID更新一条performance，用于区域联考excel成绩导入
     */
    public void updatePerformanceById(String performanceId, double sumScore, List<SubjectScoreEntry> subjectScoreList) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(performanceId));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("suc", sumScore)
                        .append("sList", MongoUtils.convert(MongoUtils.fetchDBObjectList(subjectScoreList))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, updateValue);

    }


    /**
     * 查询考试成绩数据
     *
     * @param query
     * @param fields
     * @return
     */
//    public List<ScoreEntry> getScoreEntryList(DBObject query, DBObject fields) {
//        List<ScoreEntry> scoreList = new ArrayList<ScoreEntry>();
//        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, fields,
//                new BasicDBObject());
//        if (null != list && !list.isEmpty()) {
//        	ScoreEntry e;
//            for (DBObject dbo : list) {
//                e = new ScoreEntry((BasicDBObject) dbo);
//                scoreList.add(e);
//            }
//        }
//        return scoreList;
//    }


    /**
     * 获取区域联考的学期信息
     */

    public List<String> findTrem(ObjectId eductionId) {
        List<String> list = new ArrayList<String>();
        DBObject query = new BasicDBObject()
                .append("eid", eductionId)
                .append("isr", Constant.ZERO);
        List<DBObject> db = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REGIONAL_EXAM,
                query, Constant.FIELDS);
        for (DBObject dbo : db) {
            RegionalExamEntry ren = new RegionalExamEntry((BasicDBObject) dbo);
            list.add(ren.getTerm());
        }
        return list;

    }

    /**
     * 获取学校的联考学期信息
     */

    public List<String> findSchoolTrem(String schoolId) {
        List<String> list = new ArrayList<String>();
        DBObject query = new BasicDBObject()
                .append("sId", new ObjectId(schoolId))
                .append("df", Constant.ZERO);
        List<DBObject> db = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, Constant.FIELDS);
        for (DBObject dbo : db) {
            ExamEntry ren = new ExamEntry((BasicDBObject) dbo);
            list.add(ren.getSchoolYear());
        }
        return list;

    }

    /**
     * 通过userId查询教育局id
     *
     * @param userId
     */
    public ObjectId selEducationByUserId(ObjectId userId) {
        DBObject query = new BasicDBObject("uis", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if (null != dbo) {
            EducationBureauEntry eid = new EducationBureauEntry((BasicDBObject) dbo);
            return eid.getID();
        }
        return null;
    }


    /**
     * 教育局
     * 根据学期查询区域连考信息
     */
    public List<Map<String, Object>> findByTerm(String trem, ObjectId id) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BasicDBObject orderBy = new BasicDBObject("date", Constant.DESC);
        DBObject query = new BasicDBObject()
                .append("term", trem)
                .append("eid", id)
                .append("isr", Constant.ZERO);
        List<DBObject> db = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REGIONAL_EXAM,
                query, Constant.FIELDS, orderBy);
        for (DBObject dbo : db) {
            RegionalExamEntry joint = new RegionalExamEntry((BasicDBObject) dbo);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("nm", joint.getName());
            map.put("gnm", joint.getGradeName());
            map.put("date", formatter.format(joint.getExamDate()));
            map.put("id", joint.getID().toString());
            list.add(map);
        }
        return list;
    }

    /**
     * 根据每个联考项目的id查询这次考试的所有信息
     */
    public RegionalExamEntry findByJointId(String jointId) {

        DBObject query = new BasicDBObject().append(Constant.ID, new ObjectId(jointId)).append("isr", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REGIONAL_EXAM, query, Constant.FIELDS);
        if (null != dbo) {
            return new RegionalExamEntry((BasicDBObject) dbo);
        }
        return null;

    }

    /**
     * 提交到教育局
     *
     * @param jiontExamId
     * @param schoolList
     */
    public void updatejiontExamById(String jiontExamId, List<RegionalSchItem> schoolList) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(jiontExamId));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("rf", Constant.ZERO)
                        .append("sch", MongoUtils.convert(MongoUtils.fetchDBObjectList(schoolList))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REGIONAL_EXAM, query, updateValue);

    }

    /**
     * 根据区域联考id 删除联考信息
     */

    public void delete(String jointId) {
        update(MongoFacroty.getAppDB(),
                Constant.COLLECTION_REGIONAL_EXAM,
                new BasicDBObject().append(Constant.ID, new ObjectId(jointId)),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("isr", 1)));

        update(MongoFacroty.getAppDB(),
                Constant.COLLECTION_EXAM_SUMMARY,
                new BasicDBObject().append("aid", new ObjectId(jointId)),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("isd", 1)));

        update(MongoFacroty.getAppDB(),
                Constant.COLLECTION_EXAM,
                new BasicDBObject().append("eId", new ObjectId(jointId)),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("df", 1)));

    }

    /**
     * 学校
     * 根据学期查询区域连考信息
     */
    public List<Map<String, Object>> findByschTerm(String schoolYear, ObjectId schId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BasicDBObject orderBy = new BasicDBObject("date", Constant.DESC);
        DBObject query = new BasicDBObject()
                .append("schY", schoolYear)
                .append("sId", schId)
                .append("df", Constant.ZERO)
                .append("eId", new BasicDBObject(Constant.MONGO_NE, null));
        List<DBObject> db = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : db) {
            Map<String, Object> map = new HashMap<String, Object>();
            ExamEntry ee = new ExamEntry((BasicDBObject) dbo);
            map.put("name", ee.getName());
            map.put("gna", ee.getGradeName());
            map.put("ed", formatter.format(ee.getExamDate()));
            map.put("id", ee.getID().toString());
            list.add(map);
        }
        return list;
    }

    /**
     * 根据id查询，返回map形式
     *
     * @param cos
     * @param fields
     * @return
     */
    public List<ClassEntry> getClassEntryListByObjectIds(Collection<ObjectId> idList) {
        List<ClassEntry> resultList = new ArrayList<ClassEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, idList));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_NAME, query, Constant.FIELDS);
        if (list == null) {
            return resultList;
        }
        for (DBObject obj : list) {
            resultList.add(new ClassEntry((BasicDBObject) obj));
        }
        return resultList;
    }

    /**
     * 获取区域联考统计考试信息
     */
    public ExamSummaryEntry getExamSummaryEntryByExamIdAndSchoolId(ObjectId examId, ObjectId schoolId) {
        DBObject query = new BasicDBObject("aid", examId).append("sid", schoolId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_SUMMARY, query, Constant.FIELDS);
        if (null != dbo) {
            return new ExamSummaryEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 更新统计信息相关数据(根据ID)
     */
    public void updateExamSummaryById(ObjectId id, double csAll, List<SubjectDetails> sdList) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("cs", csAll)
                        .append("sci", MongoUtils.convert(MongoUtils.fetchDBObjectList(sdList))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_SUMMARY, query, updateValue);

    }
}
