package com.db.examresult;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.examresult.ExamResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/6/12.
 */

public class ExamResultDao extends BaseDao{

    //=====================================================增================================================================
    /**
     * 新建考试
     * @param e
     * @return
     */
    public ObjectId add(ExamResultEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, e.getBaseEntry());
        return e.getID();
    }
    //=====================================================删================================================================
    /**
     * 删除考试, 逻辑删除
     * @param examId
     */
    public void delete(ObjectId examId) {
        DBObject query = new BasicDBObject(Constant.ID,examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("df", 1));
//        MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_EXAMRESULT_NAME).remove(query);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, updateValue);
    }

    /**
     * 删除参加考试的班级
     * @param examResultId
     * @param classId
     * @return
     */
    public boolean deleteClass(ObjectId examResultId, ObjectId classId) {
        DBObject query = new BasicDBObject(Constant.ID,examResultId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("cList", classId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, updateValue);
        return true;
    }

    /**
     * 删除某个performance
     * @param examResultId
     * @param pId
     * @return
     */
    public boolean deletePerformance(ObjectId examResultId, ObjectId pId) {
        DBObject query = new BasicDBObject(Constant.ID,examResultId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("pList", pId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, updateValue);
        return true;
    }
    //=====================================================改================================================================
    /**
     * 更新成绩列表
     * @param id
     * @param performanceList
     */
    public void update(ObjectId id,List<ObjectId> performanceList) {
        DBObject query = new BasicDBObject(Constant.ID,id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pList",performanceList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, updateValue);
    }

    /**
     * 更新考试信息
     * @param examId
     * @return
     */
    public boolean updateInfo(ObjectId examId, ExamResultEntry examResultEntry) {
        DBObject query = new BasicDBObject(Constant.ID,examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,examResultEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, updateValue);
        return true;
    }
    //=====================================================查================================================================

    /**
     * 查询
     * @param id
     * @return
     */
    public ExamResultEntry getExamResultEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID,id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, Constant.FIELDS);
        if(null!=dbo) {
            return new ExamResultEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 查询
     * @param name
     * @return
     */
    public List<ExamResultEntry> getExamResultEntryByName(String name) {
        DBObject query = new BasicDBObject("name",name);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty()) {
            List<ExamResultEntry> examResultEntries = new ArrayList<ExamResultEntry>();
            for(DBObject dbo:list)
            {
                ExamResultEntry e = new ExamResultEntry((BasicDBObject)dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }

    /**
     * 得到exercise关联的examResultEntry
     * @param eid
     * @return
     */
    public ExamResultEntry getExamResultEntryByEid(ObjectId eid) {
        DBObject query = new BasicDBObject("eId", eid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ExamResultEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 查询出成绩列表
     * @param id
     * @return
     */
    public List<ObjectId> getPerformanceList(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID,id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, Constant.FIELDS);
        if(null != dbo) {
            ExamResultEntry ere = new ExamResultEntry((BasicDBObject) dbo);
            return ere.getPerformanceList();
        }
        return null;
    }

    /**
     * 查询考试列表
     * @param classId
     * @param subjectId
     * @param schoolYear
     * @param isGrade
     * @return
     */
    public List<ExamResultEntry> getExamList(ObjectId gradeId, ObjectId classId, ObjectId subjectId, String schoolYear, Integer isGrade) {
        DBObject query = new BasicDBObject("df", new BasicDBObject(Constant.MONGO_NE, 1));
        if(gradeId != null) {
            query.put("gId", gradeId);
        }
        if(classId != null) {
            query.put("cList", classId);
        }
        if(subjectId != null) {
            query.put("sList", subjectId);
        }
        if(schoolYear != null) {
            query.put("schY", schoolYear);
        }
        if(isGrade != null) {
            query.put("isGra", isGrade);
        }
        List<DBObject> list = new ArrayList<DBObject>();
        if(query != null) {
             list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMRESULT_NAME, query, Constant.FIELDS);
        }
        if(null!=list && !list.isEmpty())
        {
            List<ExamResultEntry> examResultEntries = new ArrayList<ExamResultEntry>();
            for(DBObject dbo:list)
            {
                ExamResultEntry e = new ExamResultEntry((BasicDBObject)dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;
    }




}
