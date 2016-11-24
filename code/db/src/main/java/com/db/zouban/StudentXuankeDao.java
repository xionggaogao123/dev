package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by qiangm on 2015/10/8.
 */
public class StudentXuankeDao extends BaseDao {

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
     * 更新学生选课结果---finish
     *
     * @param studentChooseEntry
     */
    public void updateStudentChoose(StudentChooseEntry studentChooseEntry) {
        BasicDBObject query = new BasicDBObject("uid", studentChooseEntry.getUserId()).append("xkid", studentChooseEntry.getXuanKeId());
        BasicDBObject updateValue = new BasicDBObject()
                .append("advls", studentChooseEntry.getAdvancelist())
                .append("sipls", studentChooseEntry.getSimplelist())
                .append("cid", studentChooseEntry.getClassId())
                .append("um", studentChooseEntry.getUserName())
                .append("isx", studentChooseEntry.getIsXuan());
        BasicDBObject update = new BasicDBObject().append(Constant.MONGO_SET, updateValue);
        try {
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 学生选课数量
     *
     * @param xuankeId
     * @param isXuan
     * @return
     */
    public int findStudentChooseByType(ObjectId xuankeId, int isXuan) {
        BasicDBObject query = new BasicDBObject("xkid", xuankeId).append("isx", isXuan);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query);
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

    /**
     * 根据学科组合查出学生  shanchao
     * @param subjectIds
     * @param xuankeId
     * @return
     */
    public List<StudentChooseEntry> getStudentChoosesBySubjectGroup(Collection<ObjectId> subjectIds, ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        query.append("advls", new BasicDBObject("$all", subjectIds));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, Constant.FIELDS);
        List<StudentChooseEntry> studentChooseEntries = new ArrayList<StudentChooseEntry>();
        if (dbObjects != null) {
            for (DBObject dbObject : dbObjects) {
                studentChooseEntries.add(new StudentChooseEntry((BasicDBObject) dbObject));
            }
        }
        return studentChooseEntries;
    }

    /**
     * 查询组合选择人数
     * @param subjectIds
     * @param xuankeId
     * @return
     */
    public int countStudentChoosesBySubjectGroup(Collection<ObjectId> subjectIds, ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        query.append("advls", new BasicDBObject("$all", subjectIds));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query);
    }


    /**
     * 根据选课组合查询学生
     *
     * @param subjectIds
     * @param xuankeId
     * @return
     */
    public List<StudentChooseEntry> getStudentBySubjectGroup(Collection<ObjectId> subjectIds, ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject("xkid", xuankeId).append("advls", new BasicDBObject("$all", subjectIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, Constant.FIELDS);
        List<StudentChooseEntry> studentChooseEntries = new ArrayList<StudentChooseEntry>();

        for (DBObject dbObject : dbObjectList) {
            studentChooseEntries.add(new StudentChooseEntry((BasicDBObject) dbObject));
        }
        return studentChooseEntries;
    }

    /**
     * 选课进度学生列表
     *
     * @param xuanKeId
     * @param classId
     * @param choose
     * @param userName
     */
    public List<StudentChooseEntry> studentXuanKeList(ObjectId xuanKeId, ObjectId classId, int choose, String userName) {
        List<StudentChooseEntry> retList = new ArrayList<StudentChooseEntry>();
        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        query.append("cid", classId);
        if (!StringUtils.isEmpty(userName)) {
            Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.MULTILINE);
            query.append("um", new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        query.append("isx", choose);

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new StudentChooseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    public List<StudentChooseEntry> studentXuanKeList(ObjectId xuanKeId, ObjectId classId) {
        List<StudentChooseEntry> retList = new ArrayList<StudentChooseEntry>();
        BasicDBObject query = new BasicDBObject("xkid", xuanKeId).append("cid", classId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new StudentChooseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 获取全年级的选课列表
     *
     * @param xuanKeId
     * @return
     */
    public List<StudentChooseEntry> findStuXuanKeListByGradeId(ObjectId xuanKeId) {
        List<StudentChooseEntry> retList = new ArrayList<StudentChooseEntry>();
        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new StudentChooseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 获取分段班级选课结果
     *
     * @param xuankeId
     * @param classIds
     * @return
     */
    public List<StudentChooseEntry> findStuXuanKeListByClassIds(ObjectId xuankeId, List<ObjectId> classIds) {
        List<StudentChooseEntry> retList = new ArrayList<StudentChooseEntry>();
        BasicDBObject query = new BasicDBObject("xkid", xuankeId).append("cid", new BasicDBObject(Constant.MONGO_IN, classIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new StudentChooseEntry((BasicDBObject) dbo));
        }
        return retList;
    }


}
