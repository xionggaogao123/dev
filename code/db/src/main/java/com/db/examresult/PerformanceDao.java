package com.db.examresult;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.examresult.PerformanceEntry;
import com.pojo.examresult.Score;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by fl on 2015/6/15.
 */


public class PerformanceDao extends BaseDao{

//==================================================================增==============================================================
    /**
     * 添加一条成绩记录
     * @param performanceEntry
     * @return
     */
    public ObjectId save(PerformanceEntry performanceEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, performanceEntry.getBaseEntry());
        return performanceEntry.getID();
    }

    /**
     * 添加一批成绩记录
     * @param pList
     * @return
     */
    public List<ObjectId> save(List<PerformanceEntry> pList) {
        List<DBObject> dbObjectList = new ArrayList<DBObject>();
        List<ObjectId> performanceList = new ArrayList<ObjectId>();
        for(PerformanceEntry p : pList) {
            dbObjectList.add(p.getBaseEntry());
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME,dbObjectList );
        for(PerformanceEntry p : pList) {
            performanceList.add(p.getId());
        }
        return performanceList;
    }

    //=====================================================删================================================================
    /**
     * 删除某次考试某个班级某个学科的记录
     * @param performanceId
     * @param subjectId
     * @return
     */
    public boolean deleteSubject(ObjectId performanceId, ObjectId subjectId) {
        DBObject query = new BasicDBObject(Constant.ID,performanceId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("sList",new BasicDBObject("subId",subjectId)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, updateValue);
        return true;
    }

    /**
     * 删除
     * @param pId
     */
    public void delete(ObjectId pId) {
        DBObject query = new BasicDBObject(Constant.ID,pId);
        MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_PERFORMANCE_NAME).remove(query);
    }

    /**
     * 删除
     * @param examId
     */
    public void deleteByExamId(ObjectId examId) {
        DBObject query = new BasicDBObject("exId",examId);
        MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_PERFORMANCE_NAME).remove(query);
    }
    //=====================================================改================================================================
    /**
     * 打分,用于页面
     * @param subjectId
     * @param score
     */
    public void updateScore(ObjectId performanceId, ObjectId subjectId, Double score, Integer absence, Integer exemption) {
        DBObject query = new BasicDBObject(Constant.ID,performanceId).append("sList.subId", subjectId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sList.$.subS",score).append("sList.$.abs", absence).append("sList.$.exemp", exemption));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, updateValue);
    }

    /**
     * 打分，用于excel导入
     * @param subjectId
     * @param score
     */
    public void updateScore(ObjectId examId, ObjectId subjectId, Double score, Integer absence, Integer exemption, String stuName) {
        DBObject query = new BasicDBObject("stuNm", stuName).append("sList.subId", subjectId).append("exId", examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sList.$.subS",score).append("sList.$.abs", absence).append("sList.$.exemp", exemption));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, updateValue);
    }

    /**
     * 更新满分及格分，用于老师编辑考试信息
     * @param examId
     * @param subjectId
     * @param fullScore
     * @param failScore
     */
    public void updateFullFailScoreByExamId(ObjectId examId, ObjectId subjectId, Integer fullScore, Integer failScore) {
        DBObject query = new BasicDBObject("exId",examId).append("sList.subId", subjectId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sList.$.full",fullScore).append("sList.$.fail", failScore));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, updateValue);
    }

    /**
     * 整合考试，更新成绩
     * @param examId
     * @param stuId
     * @param score
     */
    public void updateScore(ObjectId examId, ObjectId stuId, Double score) {
        DBObject query = new BasicDBObject("exId", examId).append("stuId", stuId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sList.0.subS",score));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, updateValue);
    }

    public void updateAreaRanking(ObjectId performanceId, int ranking) {
        DBObject query = new BasicDBObject(Constant.ID, performanceId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ar",ranking));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, updateValue);
    }
    //=====================================================查================================================================

    /**
     * 根据成绩记录的id查询整条成绩记录
     * @param id
     * @return
     */
    public PerformanceEntry getPerformanceById(ObjectId id){
        DBObject query = new BasicDBObject(Constant.ID,id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new PerformanceEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 查找考试成绩,用于新表结构
     * @param examId
     * @param classId
     * @return
     */
    public List<PerformanceEntry> getPerformanceEntryList(ObjectId examId, ObjectId classId, ObjectId stuId, List<ObjectId> examList, List<ObjectId> classIds){
        BasicDBObject query = new BasicDBObject();
        if(examId != null) {
            query.put("exId", examId);
        }
        if(classId != null) {
            query.put("cId", classId);
        }
        if(stuId != null) {
            query.put("stuId", stuId);
        }
        if(examList != null) {
            query.put("exId", new BasicDBObject(Constant.MONGO_IN, examList));
        }
        if(classIds != null) {
            query.put("cId", new BasicDBObject(Constant.MONGO_IN, classIds));
        }
        List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
        if(query != null) {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, Constant.FIELDS);
            if (null != list && !list.isEmpty()) {
                PerformanceEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceEntry((BasicDBObject) dbo);
                    performanceEntryList.add(p);
                }

            }
        }
        return performanceEntryList;
    }

    /**
     * 查找考试成绩，用于旧表结构
     * @param
     * @return
     */
    public List<PerformanceEntry> getPerformanceEntryList(List<ObjectId> performanceList, ObjectId classId, ObjectId stuId){
        BasicDBObject query = new BasicDBObject();
        if(performanceList != null) {
            query.put(Constant.ID, new BasicDBObject(Constant.MONGO_IN, performanceList));
        }
        if(classId != null) {
            query.put("cId", classId);
        }
        if(stuId != null) {
            query.put("stuId", stuId);
        }
        List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
        if(query != null) {
            List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, Constant.FIELDS);
            if (null != list && !list.isEmpty()) {

                PerformanceEntry p;
                for (DBObject dbo : list) {
                    p = new PerformanceEntry((BasicDBObject) dbo);
                    performanceEntryList.add(p);
                }

            }
        }
        return performanceEntryList;
    }

    /**
     * 查询某科目满分
     * @param performanceId
     * @param subjectId
     * @return
     */
   public Integer getFullScore(ObjectId performanceId, ObjectId subjectId) {
       DBObject query = new BasicDBObject(Constant.ID,performanceId);
       BasicDBObject dbo = (BasicDBObject)findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, Constant.FIELDS);
       if(null!=dbo)
       {
           PerformanceEntry performanceEntry = new PerformanceEntry(dbo);
           for(Score score : performanceEntry.getScoreList()) {
               if (score.getSubjectId().equals(subjectId)) {
                   return score.getFullScore();
               }
           }
       }
       return null;
   }

    /**
     * 查询某科目及格分
     * @param performanceId
     * @param subjectId
     * @return
     */
    public Integer getFailScore(ObjectId performanceId, ObjectId subjectId) {
        DBObject query = new BasicDBObject(Constant.ID,performanceId);
        BasicDBObject dbo = (BasicDBObject)findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            PerformanceEntry performanceEntry = new PerformanceEntry(dbo);
            for(Score score : performanceEntry.getScoreList()) {
                if (score.getSubjectId().equals(subjectId)) {
                    return score.getFailScore();
                }
            }
        }
        return null;
    }

    /**
     * 查找考试超过某个分数的学生数
     * @param examId
     * @param subjectId
     * @param score
     * @return
     */
    public int findPassCount(ObjectId examId, ObjectId subjectId, int score){
        DBObject query = new BasicDBObject("exId", examId)
                .append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH,
                        new BasicDBObject("subId", subjectId).append("subS",
                                new BasicDBObject(Constant.MONGO_GTE, score))));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query);
    }

    /**
     * 查找考试超过某个分数的学生数
     * @param examList
     * @param subjectId
     * @param score
     * @return
     */
    public int findPassCount(List<ObjectId> examList, ObjectId subjectId, int score){
        DBObject query = new BasicDBObject("exId", new BasicDBObject(Constant.MONGO_IN, examList))
                .append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH,
                        new BasicDBObject("subId", subjectId).append("subS",
                                new BasicDBObject(Constant.MONGO_GTE, score))));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query);
    }

    /**
     * 成绩列表
     * @param examId
     * @return
     */
    public List<PerformanceEntry> findPerformanceList(ObjectId examId){
        DBObject query = new BasicDBObject("exId", examId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
            PerformanceEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return null;
    }

    /**
     * 区域联考学生成绩排名表
     * @param areaId
     * @param skip
     * @param pageSize
     * @return
     */
    public List<PerformanceEntry> findPerformanceListByAreaId(ObjectId areaId, int skip, int pageSize){
        DBObject matchDBO=new BasicDBObject("aid",areaId);
        List<DBObject> list = find(MongoFacroty.getAppDB(),Constant.COLLECTION_PERFORMANCE_NAME, matchDBO,Constant.FIELDS,new BasicDBObject("ar",Constant.ASC),skip,pageSize);
        if (null != list && !list.isEmpty()) {
            List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
            PerformanceEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return null;
    }

    public List<PerformanceEntry> findPerformanceListByAreaId(ObjectId areaId,List<ObjectId> examIds, DBObject fields){
        BasicDBObject query=new BasicDBObject("aid",areaId);
        if(examIds != null){
            query.append("exId", new BasicDBObject(Constant.MONGO_IN, examIds));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(),Constant.COLLECTION_PERFORMANCE_NAME,query, fields);
        if (null != list && !list.isEmpty()) {
            List<PerformanceEntry> performanceEntryList = new ArrayList<PerformanceEntry>();
            PerformanceEntry p;
            for (DBObject dbo : list) {
                p = new PerformanceEntry((BasicDBObject) dbo);
                performanceEntryList.add(p);
            }
            return performanceEntryList;
        }
        return null;
    }

    public int findPerformanceCountByAreaId(ObjectId areaExamId){
        DBObject query = new BasicDBObject("aid", areaExamId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_PERFORMANCE_NAME, query);
    }


}
