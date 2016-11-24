package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.utils.DeleteState;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/10/8.
 */
public class StudentXuankeDao extends BaseDao {

    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();

    /**
     * 插入学生选课记录
     *
     * @param studentChooseEntry
     */
    public void addStudentChooseEntry(StudentChooseEntry studentChooseEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, studentChooseEntry.getBaseEntry());
    }

    /**
     * 删除学生选课记录
     *
     * @param xuankeId
     */
    public void removeStudentChooseEntry(ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject("xkid", xuankeId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query);
    }

    /**
     * 添加学生选课结果---finish
     *
     * @param studentChooseEntry
     */
    public void addStudentChoose(StudentChooseEntry studentChooseEntry) {
        //如果存在先删除
        BasicDBObject query = new BasicDBObject("uid", studentChooseEntry.getUserId()).append("xkid", studentChooseEntry.getXuanKeId());
        BasicDBObject updateValue = new BasicDBObject();
        /*updateValue.append("xkid",studentChooseEntry.getXuanKeId());
        updateValue.append("uid",studentChooseEntry.getUserId());*/
        updateValue.append("advls", studentChooseEntry.getAdvancelist());
        updateValue.append("sipls", studentChooseEntry.getSimplelist());
        updateValue.append("cid", studentChooseEntry.getClassId());
        updateValue.append("um", studentChooseEntry.getUserName());
        updateValue.append("isx", studentChooseEntry.getIsXuan());
        BasicDBObject updateValue2 = new BasicDBObject().append(Constant.MONGO_SET, updateValue);
        try {
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, updateValue2);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 学生选课数量选课
     *
     * @param xuankeId
     * @param isXuan
     * @return
     */
    public int findStudentChooseByType(ObjectId xuankeId, int isXuan) {
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        query.append("isx", isXuan);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query);
    }

    /**
     * 等级考中添加学生名单-------finish
     *
     * @param courseIds
     * @param stuId
     * @param classId
     */
    public void addAdvanceStudent(List<ObjectId> courseIds, ObjectId stuId, ObjectId classId, ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject("subid", new BasicDBObject(Constant.MONGO_IN, courseIds));
        query.append("xkid", xuankeId);
        BasicDBObject updateValue = new BasicDBObject();
        IdValuePair idValuePair = new IdValuePair(stuId, classId);
        updateValue.append(Constant.MONGO_PUSH, new BasicDBObject("ausers", idValuePair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }

    /**
     * 合格考中添加学生名单-----finish
     *
     * @param courseIds
     * @param stuId
     * @param classId
     */
    public void addSimpleStudent(List<ObjectId> courseIds, ObjectId stuId, ObjectId classId, ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject("subid", new BasicDBObject(Constant.MONGO_IN, courseIds));
        query.append("xkid", xuankeId);
        BasicDBObject updateValue = new BasicDBObject();
        IdValuePair idValuePair = new IdValuePair(stuId, classId);
        updateValue.append(Constant.MONGO_PUSH, new BasicDBObject("susers", idValuePair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }

    /**
     * 删除subjectConf表中已经存在的选课记录----finish
     *
     * @param stuId
     * @param classId
     * @param advanceList
     * @param simpleList
     */
    public void removeXuankeHistory(ObjectId xuankeId, ObjectId stuId, ObjectId classId, List<ObjectId> advanceList, List<ObjectId> simpleList) {
        advanceList.addAll(simpleList);
        BasicDBObject query = new BasicDBObject("subid", new BasicDBObject(Constant.MONGO_IN, advanceList));
        query.append("xkid", xuankeId);
        //BasicDBObject updateValue=new BasicDBObject();
        IdValuePair idValuePair = new IdValuePair(stuId, classId);
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("ausers", idValuePair.getBaseEntry())));
        basicDBList.add(new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("susers", idValuePair.getBaseEntry())));
        //updateValue.append(Constant.MONGO_AND,basicDBList);
        try {
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("ausers", idValuePair.getBaseEntry())));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("susers", idValuePair.getBaseEntry())));
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * 获取学生已经选择的走班科目----finish
     *
     * @param stuId
     * @param xuankeId
     * @return
     */
    public StudentChooseEntry getStudentChoose(ObjectId stuId, ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        query.append("uid", stuId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, Constant.FIELDS);
        return new StudentChooseEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取学生选择的走班课
     *
     * @param stuId
     * @param term
     * @param gradeId
     * @return
     */
    public List<ZouBanCourseEntry> getStudentChooseZB(ObjectId stuId, String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("stus", stuId);
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 获取学生选课进度
     *
     * @param classId
     * @param term
     * @return
     */
    public List<StudentChooseEntry> getStudentChoose(ObjectId classId, String term) {
        List<StudentChooseEntry> studentChooseEntryList = new ArrayList<StudentChooseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("cid", classId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, Constant.FIELDS);
        for (DBObject dbObject : dbObjectList) {
            StudentChooseEntry studentChooseEntry = new StudentChooseEntry((BasicDBObject) dbObject);
            studentChooseEntryList.add(studentChooseEntry);
        }
        return studentChooseEntryList;
    }

    /**
     * 获取学生选课单科名单
     *
     * @param xuankeId
     * @param type
     * @param subjectId
     * @return
     */
    public SubjectConfEntry findSubjectConf(ObjectId xuankeId, int type, ObjectId subjectId) {
        List<DBObject> list = new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("delflg", DeleteState.NORMAL.getState());
        query.append("xkid", xuankeId);
        query.append("subid", subjectId);
        query.append("type", type);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, Constant.FIELDS);
        SubjectConfEntry retList = new SubjectConfEntry((BasicDBObject) dbObject);

        return retList;
    }

    /**
     * 获取学生需要选择的走班课列表
     *
     * @param xuankeId
     * @return
     */
    public List<SubjectConfEntry> getCourseConfList(ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, Constant.FIELDS);
        List<SubjectConfEntry> courseConfEntries = new ArrayList<SubjectConfEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject d : dbObjectList) {
                courseConfEntries.add(new SubjectConfEntry((BasicDBObject) d));
            }
        }
        return courseConfEntries;
    }


    /**
     * 3+3考务管理使用   shanchao
     *
     * @param stuIds
     * @param xuankeId
     * @return
     */
    public List<StudentChooseEntry> getStudentChooses(List<ObjectId> stuIds, ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        query.append("uid", new BasicDBObject(Constant.MONGO_IN, stuIds));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, Constant.FIELDS);
        List<StudentChooseEntry> studentChooseEntries = new ArrayList<StudentChooseEntry>();
        if (dbObjects != null) {
            for (DBObject dbObject : dbObjects) {
                studentChooseEntries.add(new StudentChooseEntry((BasicDBObject) dbObject));
            }
        }
        return studentChooseEntries;
    }


}
