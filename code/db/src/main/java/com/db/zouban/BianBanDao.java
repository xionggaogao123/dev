package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wang_xinxin on 2015/10/10.
 */
public class BianBanDao extends BaseDao {
    /**
     * 分段列表
     *
     * @param xuanKeId
     * @return
     */
    public List<ClassFengDuanEntry> findFengDuanList(ObjectId xuanKeId) {
        List<ClassFengDuanEntry> retList = new ArrayList<ClassFengDuanEntry>();
        List<DBObject> list = new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, null);
        for (DBObject dbo : list) {
            retList.add(new ClassFengDuanEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 分段
     *
     * @param xuanKeId
     * @param group
     * @return
     */
    public ClassFengDuanEntry findGroupList(ObjectId xuanKeId, int group) {
        BasicDBObject query = new BasicDBObject("xkid", xuanKeId).append("group", group);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, null);
        if (null != dbo) {
            return new ClassFengDuanEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * @param xuanKeId
     * @param users
     * @return
     */
    public List<StudentChooseEntry> findStudentChooseList(ObjectId xuanKeId, Collection<ObjectId> users) {
        List<StudentChooseEntry> retList = new ArrayList<StudentChooseEntry>();
        List<DBObject> list = new ArrayList<DBObject>();

        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        query.append("uid", new BasicDBObject(Constant.MONGO_IN, users));
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new StudentChooseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 删除分段里的班级
     *
     * @param xuanKeId
     * @param group
     * @param clsid
     */
    public void updateClassGroup(ObjectId xuanKeId, Integer group, ObjectId clsid) {
        BasicDBObject query = new BasicDBObject("xkid", xuanKeId).append("group", group);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cids", clsid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, updateValue);

    }

    /**
     * 更新分段里的班级
     *
     * @param groupId
     * @param clids
     */
    public void updateFengDuanClass(ObjectId groupId, List<ObjectId> clids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cids", clids));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, updateValue);
    }

    /**
     * 班级排名
     *
     * @param gradeId
     * @param schoolId
     * @return
     */
    public List<GradeClassSoreEntry> findClassPaiMing(String examId, String gradeId, String schoolId) {
        List<GradeClassSoreEntry> retList = new ArrayList<GradeClassSoreEntry>();
        List<DBObject> list = new ArrayList<DBObject>();

        BasicDBObject query = new BasicDBObject("gid", new ObjectId(gradeId));
        query.append("sid", new ObjectId(schoolId));
        query.append("exid", new ObjectId(examId));
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_PAIMING, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new GradeClassSoreEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 插入排名
     *
     * @param gradeClassSoreEntry
     */
    public void addClassPaiMing(GradeClassSoreEntry gradeClassSoreEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_PAIMING, gradeClassSoreEntry.getBaseEntry());
    }

    /**
     * 添加分段
     *
     * @param classFengDuanEntry
     */
    public void addFengDuan(ClassFengDuanEntry classFengDuanEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, classFengDuanEntry.getBaseEntry());
    }

    /**
     * 分段
     *
     * @param groupId
     * @return
     */
    public ClassFengDuanEntry findFenDuanListById(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, null);
        if (null != dbo) {
            return new ClassFengDuanEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 分层
     *
     * @param xuanKeId
     * @param groupId
     * @param subjectConfId
     * @param type
     * @return
     */
    public FenCengEntry findFenCengCount(String xuanKeId, String groupId, ObjectId subjectConfId, int type) {

        BasicDBObject query = new BasicDBObject("xkid", new ObjectId(xuanKeId));
        query.append("grpid", new ObjectId(groupId));
        query.append("scid", subjectConfId);
        query.append("type", type);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FENCENG, query, null);
        if (null != dbo) {
            return new FenCengEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 插入分层
     *
     * @param fenceng
     */
    public ObjectId addFenCengEntry(FenCengEntry fenceng) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FENCENG, fenceng.getBaseEntry());
        return fenceng.getID();
    }

    /**
     * 更新分层
     *
     * @param fenceng
     */
    public void updateFenCengEntry(FenCengEntry fenceng) {
        DBObject query = new BasicDBObject(Constant.ID, fenceng.getID());
        DBObject value = new BasicDBObject("fcim", MongoUtils.convert(MongoUtils.fetchDBObjectList(fenceng.getFenCengItemList())));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FENCENG, query, updateValue);
    }


    /**
     * 分段列表
     *
     * @param groupIds
     * @param fields
     * @return
     */
    public Map<ObjectId, ClassFengDuanEntry> findGroupEntryMap(Collection<ObjectId> groupIds, DBObject fields) {
        Map<ObjectId, ClassFengDuanEntry> retMap = new HashMap<ObjectId, ClassFengDuanEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, groupIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, fields);
        if (null != list && !list.isEmpty()) {
            ClassFengDuanEntry e;
            for (DBObject dbo : list) {
                e = new ClassFengDuanEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 删除分段
     *
     * @param xuanKeId
     */
    public void removeFenDuan(String xuanKeId) {
        DBObject query = new BasicDBObject("xkid", new ObjectId(xuanKeId));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query);
    }

    /**
     * 重命名
     *
     * @param courseId
     * @param courseName
     */
    public void updateName(ObjectId courseId, String courseName) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("clsnm", courseName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }
}
