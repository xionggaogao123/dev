package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.InterestClassLessonScoreEntry;
import com.pojo.school.InterestClassStudent;
import com.pojo.school.InterestClassTranscriptEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hao on 2015/3/20.
 */
public class InterestClassDao extends BaseDao {

    /*
    *
    * 添加扩展课
    * */
    public ObjectId addExpandClass(InterestClassEntry expandClassEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, expandClassEntry.getBaseEntry());
        return expandClassEntry.getID();
    }


    public void saveTranscript(InterestClassTranscriptEntry transcriptEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_TRANSCRIPT, transcriptEntry.getBaseEntry());
    }

    /*
    *
    * 更新扩展课信息
    *
    * */
    public void updateExpandClass(InterestClassEntry expandClassEntry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, expandClassEntry.getID());
        BasicDBObject update = new BasicDBObject("cn", expandClassEntry.getClassName()).
                append("tid", expandClassEntry.getTeacherId()).
                append("tc", expandClassEntry.getTotalCount()).
                append("ct", expandClassEntry.getClassTime()).
                append("sid", expandClassEntry.getSubjectId()).
                append("ot", expandClassEntry.getOpenTime()).
                append("clt", expandClassEntry.getCloseTime()).
                append("gl", expandClassEntry.getGradeIds()).
                append("tt", expandClassEntry.getTermType()).
                append("clc", expandClassEntry.getClassContent()).
                append("rm", expandClassEntry.getRoom()).
                append("tm", MongoUtils.convert(MongoUtils.fetchDBObjectList(expandClassEntry.getTerm())));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);
    }

    /*
    * 根据id 删除扩展课
    * */
    public void deleteExpandClassById(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
//        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, update);
    }

    /*
    * 更新状态
    *
    * */
    public void updateExpandState(ObjectId objectId, int state) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("stat", state));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, update);
    }

    /*
    * 查询当前扩展课可用状态
    *
    * */
    public int findState(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId).append("ir", new BasicDBObject("$ne", 1));
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, new BasicDBObject("stat", 1));
        InterestClassEntry expandClassEntry = new InterestClassEntry((BasicDBObject) dbObject);
        return expandClassEntry.getState();
    }

    /*
    *
    * 拓展课添加学生
    * */
    public void addStudent2ExpandClass(ObjectId expandClassId, ObjectId studentId, int courseType, int termType, int dropState) {
        BasicDBObject query = new BasicDBObject(Constant.ID, expandClassId);
        InterestClassStudent studentEntry = new InterestClassStudent();
        studentEntry.setStudentId(studentId);
        studentEntry.setCourseType(courseType);
        studentEntry.setTermType(termType);
        studentEntry.setDropState(dropState);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("stl", studentEntry.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);
    }

    /*
    *
    * 拓展课删除学生
    *
    * */
    public void deleteStudentFromExpandClass(ObjectId expandClassId, ObjectId studentId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, expandClassId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("stl", new BasicDBObject("sid", studentId)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);
    }

    public void deleteStudentFromAllExpandClass(ObjectId studentId) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("stl", new BasicDBObject("sid", studentId)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);
    }

    /*
    *
    * 依据学校id 找到所有拓展课（兴趣班）
    *
    * */
    public List<InterestClassEntry> findClassBySchoolId(ObjectId schoolId, int termType, String typeId, ObjectId gradeId) {
//        BasicDBObject query=new BasicDBObject("sid",schoolId).append("tt",termType);
        BasicDBObject query = new BasicDBObject("sld", schoolId).append("clt", new BasicDBObject(Constant.MONGO_GTE, 1435719600000l)).append("ir", new BasicDBObject("$ne", 1));

        if (termType > -1) {
            query.append("tm.v", termType);
        }
        if (typeId != null) {
            if (typeId.equals("")) {
                query.append("tyid", null);
            } else {
                query.append("tyid", new ObjectId(typeId));
            }
        }
        if (null != gradeId) {
            query.append("gl", gradeId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> classEntryList = new ArrayList<InterestClassEntry>();
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                InterestClassEntry classEntry = new InterestClassEntry((BasicDBObject) dbObject);
                classEntryList.add(classEntry);
            }
        }
        return classEntryList;
    }

    /*
    *
    * 依据老師id 找到所有拓展课（兴趣班）
    *
    * */
    public List<InterestClassEntry> getInterestClassEntryByTeacherId(ObjectId userId, int termType, String typeId, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("tid", userId).append("clt", new BasicDBObject(Constant.MONGO_GTE, 1435719600000l)).append("ir", new BasicDBObject("$ne", 1));
        if (termType > -1) {
            query.append("tm.v", termType);
        }
        if (typeId != null) {
            if (typeId.equals("")) {
                query.append("tyid", null);
            } else {
                query.append("tyid", new ObjectId(typeId));
            }
        }
        if (null != gradeId) {
            query.append("gl", gradeId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> classEntryList = new ArrayList<InterestClassEntry>();
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                InterestClassEntry classEntry = new InterestClassEntry((BasicDBObject) dbObject);
                classEntryList.add(classEntry);
            }
        }
        return classEntryList;
    }

    public List<InterestClassEntry> findClassBySchoolIdState(ObjectId schoolId, int termType, String typeId, ObjectId gradeId) {
//        BasicDBObject query=new BasicDBObject("sid",schoolId).append("tt",termType);
        BasicDBObject query = new BasicDBObject("sld", schoolId).append("clt", new BasicDBObject(Constant.MONGO_GTE, 1435719600000l)).append("ir", new BasicDBObject("$ne", 1));
        query.append("stat", 1);

        if (termType > -1) {
            query.append("tm.v", termType);
        }
        if (typeId != null) {
            if (typeId.equals("")) {
                query.append("tyid", null);
            } else {
                query.append("tyid", new ObjectId(typeId));
            }
        }
        if (null != gradeId) {
            query.append("gl", gradeId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> classEntryList = new ArrayList<InterestClassEntry>();
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                InterestClassEntry classEntry = new InterestClassEntry((BasicDBObject) dbObject);
                classEntryList.add(classEntry);
            }
        }
        return classEntryList;
    }

    /*
    * id 查询entry
    *
    * */
    public InterestClassEntry findEntryByClassId(ObjectId classID) {
        BasicDBObject query = new BasicDBObject(Constant.ID, classID).append("ir", new BasicDBObject("$ne", 1));
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        InterestClassEntry interestClassEntry = null;
        if (dbObject != null) {
            interestClassEntry = new InterestClassEntry((BasicDBObject) dbObject);
        }
        return interestClassEntry;
    }

    public List<InterestClassEntry> findClassInfoByStuId(ObjectId stuId) {
        BasicDBObject query = new BasicDBObject("stl.sid", stuId).append("clt", new BasicDBObject(Constant.MONGO_GTE, 1435719600000l));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> classEntryList = new ArrayList<InterestClassEntry>();
        for (DBObject dbObject : list) {
            InterestClassEntry classEntry = new InterestClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    public List<InterestClassEntry> findClassInfoByTeacherId(ObjectId teacherId, int termType) {
        BasicDBObject query = new BasicDBObject("tid", teacherId).append("clt", new BasicDBObject(Constant.MONGO_GTE, 1435719600000l)).append("ir", new BasicDBObject("$ne", 1));
        query.append("stat", 1);
//        if(termType!=-1){
//            query.append("tt",termType);
//        }
        if (termType > -1) {
            query.append("tm.v", termType);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> classEntryList = new ArrayList<InterestClassEntry>();
        for (DBObject dbObject : list) {
            InterestClassEntry classEntry = new InterestClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }
    //查找兴趣班所开的课时 数量
//    public Collection<Integer> countLessonsByClassId(ObjectId classId) {
//        BasicDBObject query=new BasicDBObject("cid",classId);
//        List<DBObject> list=find(MongoFacroty.getAppDB(),Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME,query,Constant.FIELDS);
//        Set<Integer> lessonIndexSet=new HashSet<Integer>();
//        Map<Integer, String> map = new HashMap<Integer, String>();
//        for(DBObject dbObject:list){
//            InterestClassLessonScoreEntry entry = new InterestClassLessonScoreEntry((BasicDBObject)dbObject);
//            lessonIndexSet.add(entry.getLessonIndex());
//            map.put(entry.getLessonIndex(), entry.getLessonName());
//        }
//        return lessonIndexSet;
//    }

    //获取兴趣班学生得分记录
    public List<InterestClassLessonScoreEntry> findLessonScoreByClassId(ObjectId classId, int termType) {
        BasicDBObject query = new BasicDBObject("cid", classId);
        if (termType > 0) {
            query.append("tt", termType);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, Constant.FIELDS);
        List<InterestClassLessonScoreEntry> lessonScoreEntryList = new ArrayList<InterestClassLessonScoreEntry>();
        for (DBObject dbObject : list) {
            InterestClassLessonScoreEntry interestClassLessonScoreEntry = new InterestClassLessonScoreEntry((BasicDBObject) dbObject);
            lessonScoreEntryList.add(interestClassLessonScoreEntry);
        }
        return lessonScoreEntryList;
    }

    public List<InterestClassLessonScoreEntry> findLessonScoreByClassIdAndIndex(ObjectId classId, Integer index) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("li", index);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, Constant.FIELDS);
        List<InterestClassLessonScoreEntry> lessonScoreEntryList = new ArrayList<InterestClassLessonScoreEntry>();
        for (DBObject dbObject : list) {
            InterestClassLessonScoreEntry interestClassLessonScoreEntry = new InterestClassLessonScoreEntry((BasicDBObject) dbObject);
            lessonScoreEntryList.add(interestClassLessonScoreEntry);
        }
        return lessonScoreEntryList;
    }

    public InterestClassLessonScoreEntry findLessonScoreEntry(ObjectId classId, ObjectId stuId, Integer index) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("uid", stuId).append("li", index);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, Constant.FIELDS);
        if (dbo != null) {
            return new InterestClassLessonScoreEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public List<InterestClassLessonScoreEntry> findStuLessonScore(ObjectId classId, ObjectId stuId, int termType) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("uid", stuId).append("tt", termType);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, Constant.FIELDS, new BasicDBObject("wi", 1));
        List<InterestClassLessonScoreEntry> lessonScoreEntryList = new ArrayList<InterestClassLessonScoreEntry>();
        for (DBObject dbObject : list) {
            InterestClassLessonScoreEntry interestClassLessonScoreEntry = new InterestClassLessonScoreEntry((BasicDBObject) dbObject);
            lessonScoreEntryList.add(interestClassLessonScoreEntry);
        }
        return lessonScoreEntryList;
    }

    public List<InterestClassLessonScoreEntry> findLessonScoreEntry(ObjectId classId, int termType, int weekIndex) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("tt", termType).append("wi", weekIndex);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, Constant.FIELDS, new BasicDBObject("li", 1));
        List<InterestClassLessonScoreEntry> lessonScoreEntryList = new ArrayList<InterestClassLessonScoreEntry>();
        for (DBObject dbObject : list) {
            InterestClassLessonScoreEntry interestClassLessonScoreEntry = new InterestClassLessonScoreEntry((BasicDBObject) dbObject);
            lessonScoreEntryList.add(interestClassLessonScoreEntry);
        }
        return lessonScoreEntryList;
    }

    public int findLessonScoreMaxIndex(ObjectId classId) {
        BasicDBObject query = new BasicDBObject("cid", classId);
        List<DBObject> list = MongoFacroty.getAppDB().
                getCollection(Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME).
                find(query).
                sort(new BasicDBObject("li", -1)).
                limit(1).
                toArray();
        if (list == null || list.isEmpty()) return 0;
        InterestClassLessonScoreEntry interestClassLessonScoreEntry = new InterestClassLessonScoreEntry((BasicDBObject) list.get(0));
        int maxIndex = interestClassLessonScoreEntry.getLessonIndex();
        return maxIndex;
    }

    public void saveLessonScore(InterestClassLessonScoreEntry lessonScoreEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, lessonScoreEntry.getBaseEntry());
    }

    public void updateLessonName(ObjectId classId, Integer index, String lessonName, int weekIndex) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("li", index);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ln", lessonName).append("wi", weekIndex));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, updateValue);
    }

    public void deleteLessonScoreByClassIdAndLessonIndex(ObjectId classId, Integer lessonIndex, Integer termType) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("li", lessonIndex);
        List<Object> termTypeList = new ArrayList<Object>();
        termTypeList.add(null);
        if (termType != null) {
            termTypeList.add(termType);
            query.append("tt", new BasicDBObject(Constant.MONGO_IN, termTypeList));
        }
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query);
    }

    public List<InterestClassTranscriptEntry> findTranscriptByClassId(ObjectId classId) {
        BasicDBObject query = new BasicDBObject("cid", classId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_TRANSCRIPT, query, Constant.FIELDS);
        List<InterestClassTranscriptEntry> transcriptEntryList = new ArrayList<InterestClassTranscriptEntry>();
        for (DBObject dbObject : list) {
            InterestClassTranscriptEntry transcriptEntry = new InterestClassTranscriptEntry((BasicDBObject) dbObject);
            transcriptEntryList.add(transcriptEntry);
        }
        return transcriptEntryList;
    }

    public InterestClassTranscriptEntry findTranscriptByUserIdAndClassId(ObjectId userId, ObjectId classId) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("uid", userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_TRANSCRIPT, query, Constant.FIELDS);
        if (dbObject != null) {
            return new InterestClassTranscriptEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    public void updateTransPicById(ObjectId transcriptId, String url) {
        BasicDBObject query = new BasicDBObject(Constant.ID, transcriptId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("rpu", url));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_TRANSCRIPT, query, update);
    }


    public List<InterestClassLessonScoreEntry> findLessonScoreByClassIdAndUserId(ObjectId classId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("cid", classId).append("uid", userId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, Constant.FIELDS, new BasicDBObject("li", 1));
        List<InterestClassLessonScoreEntry> lessonScoreEntryList = new ArrayList<InterestClassLessonScoreEntry>();
        for (DBObject dbObject : list) {
            InterestClassLessonScoreEntry interestClassLessonScoreEntry = new InterestClassLessonScoreEntry((BasicDBObject) dbObject);
            lessonScoreEntryList.add(interestClassLessonScoreEntry);
        }
        return lessonScoreEntryList;
    }

    public void updateTransSelectiveById(InterestClassTranscriptEntry transcriptEntry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, transcriptEntry.getID());
        BasicDBObject updateValue = new BasicDBObject("uid", transcriptEntry.getUserId());

        if (!StringUtils.isBlank(transcriptEntry.getResultPicUrl())) {
            updateValue.append("rpu", transcriptEntry.getResultPicUrl());
        }
        if (!StringUtils.isBlank(transcriptEntry.getTeacherComment())) {
            updateValue.append("tc", transcriptEntry.getTeacherComment());
        }
        if (transcriptEntry.getFinalResult() != 0) {
            updateValue.append("fr", transcriptEntry.getFinalResult());
        }
        if (transcriptEntry.getTotalLessonScore() != 0) {
            updateValue.append("tls", transcriptEntry.getTotalLessonScore());
        }

        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_TRANSCRIPT, query, update);
    }

    public void updateInterestClassLessonScore(InterestClassLessonScoreEntry entry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, entry.getID());
        BasicDBObject updateValue = new BasicDBObject();

        if (!StringUtils.isBlank(entry.getLessonPictureUrl())) {
            updateValue.append("lpu", entry.getLessonPictureUrl());
        }
        if (!StringUtils.isBlank(entry.getTeacherComment())) {
            updateValue.append("tc", entry.getTeacherComment());
        }
        if (entry.getAttendance() != null) {
            updateValue.append("at", entry.getAttendance());
        }
        if (entry.getStudentScore() != null) {
            updateValue.append("ss", entry.getStudentScore());
        }

        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_LESSON_SCORE_NAME, query, update);
    }

    public List<InterestClassEntry> findInterestClassOfSchool(ObjectId schoolID, int termType, ObjectId gradeId, Boolean stateFlag, String typeId) {
        BasicDBObject query = new BasicDBObject("sld", schoolID).append("clt", new BasicDBObject(Constant.MONGO_GTE, 1435719600000l)).append("ir", new BasicDBObject("$ne", 1));
        if (stateFlag) {
            query.append("stat", 1);
        }
        if (null != gradeId) {
            query.append("gl", gradeId);
        }
        if (termType > -1) {
            query.append("tm.v", termType);
        }
        if (typeId != null) {
            if (typeId.equals("")) {
                query.append("tyid", null);
            } else {
                query.append("tyid", new ObjectId(typeId));
            }
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> interestClassEntryList = new ArrayList<InterestClassEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                InterestClassEntry interestClassEntry = new InterestClassEntry((BasicDBObject) dbObject);
                interestClassEntryList.add(interestClassEntry);
            }
        }
        return interestClassEntryList;
    }

    public List<InterestClassEntry> findClassByGradeId(ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("gl", gradeId).append("ir", new BasicDBObject("$ne", 1));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> interestClassEntryList = new ArrayList<InterestClassEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                InterestClassEntry interestClassEntry = new InterestClassEntry((BasicDBObject) dbObject);
                interestClassEntryList.add(interestClassEntry);
            }
        }
        return interestClassEntryList;
    }

    public ObjectId findInterestClassId(InterestClassEntry interestClassEntry) {
        BasicDBObject query = new BasicDBObject();
        query.append("cn", interestClassEntry.getClassName()).
                append("tid", interestClassEntry.getTeacherId()).
                append("sid", interestClassEntry.getSubjectId());

        Object interestClassEntry1 = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        if (interestClassEntry1 != null) {
            return new InterestClassEntry((BasicDBObject) interestClassEntry1).getID();
        }
        return null;
    }


    /**
     * 通过ID查询
     *
     * @param ids
     * @param fields
     * @return
     */
    public List<InterestClassEntry> findInterestClassEntrysByIds(Collection<ObjectId> ids, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, fields);
        List<InterestClassEntry> interestClassEntryList = new ArrayList<InterestClassEntry>();
        if (list != null) {
            for (DBObject dbObject : list) {
                InterestClassEntry interestClassEntry = new InterestClassEntry((BasicDBObject) dbObject);
                interestClassEntryList.add(interestClassEntry);
            }
        }
        return interestClassEntryList;
    }

    /*
    *
    * 更新扩展课信息
    *
    * */
    public void updateExpandClassRelation(ObjectId cls1, ObjectId cls2) {
        BasicDBObject query = new BasicDBObject(Constant.ID, cls1);
        BasicDBObject update = new BasicDBObject("rid", cls2);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);

    }

    /**
     * 查找所有的拓展课
     *
     * @return
     */
    public List<InterestClassEntry> findAllInterestClass() {
        BasicDBObject query = new BasicDBObject().append("ir", new BasicDBObject("$ne", 1));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, Constant.FIELDS);
        List<InterestClassEntry> classEntryList = new ArrayList<InterestClassEntry>();
        for (DBObject dbObject : list) {
            InterestClassEntry classEntry = new InterestClassEntry((BasicDBObject) dbObject);
            classEntryList.add(classEntry);
        }
        return classEntryList;
    }

    public void updateTerm(ObjectId classId, IdNameValuePair term) {
        BasicDBObject query = new BasicDBObject(Constant.ID, classId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("tm", term.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);
    }

    public void updateTypeId(ObjectId classId, ObjectId typeId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, classId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tyid", typeId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);
    }

    public void deleteExpandClassBySchoolId(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sld", schoolId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query);
    }

    public void deleteKey(String key) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject updateValue = new BasicDBObject("$unset", new BasicDBObject(key, 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEREST_CLASS_NAME, query, updateValue);
    }
}
