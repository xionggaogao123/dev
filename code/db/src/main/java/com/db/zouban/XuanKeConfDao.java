package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.StudentChooseEntry;
import com.pojo.zouban.SubjectConfEntry;
import com.pojo.zouban.XuankeConfEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 选课配置
 * Created by wang_xinxin on 2015/9/22.
 */
public class XuanKeConfDao extends BaseDao {


    /**
     * 添加走班选课
     *
     * @param xuankeEntry
     * @return
     */
    public ObjectId addXuanKeConf(XuankeConfEntry xuankeEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, xuankeEntry.getBaseEntry());
        return xuankeEntry.getID();
    }

    /**
     * 添加学科
     *
     * @param subjectConfEntry
     * @return
     */
    public ObjectId addSubjectConf(SubjectConfEntry subjectConfEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, subjectConfEntry.getBaseEntry());
        return subjectConfEntry.getID();
    }

    /**
     * 学科
     *
     * @param subjectConfId
     * @return
     */
    public SubjectConfEntry findSubjectConfEntry(ObjectId subjectConfId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, subjectConfId).append("dflg", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, null);
        if (null != dbo) {
            return new SubjectConfEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 走班选课列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public XuankeConfEntry findXuanKeConf(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId).append("dflg", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, null);
        if (null != dbo) {
            return new XuankeConfEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 通过Id查询走班选课
     *
     * @param xuanKeId
     * @return
     */
    public XuankeConfEntry findXuanKeConfByXuanKeId(ObjectId xuanKeId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, xuanKeId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, null);
        if (null != dbo) {
            return new XuankeConfEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 科目列表
     *
     * @param xuankeId
     * @return
     */
    public List<SubjectConfEntry> findSubjectConf(ObjectId xuankeId, int type) {
        List<SubjectConfEntry> retList = new ArrayList<SubjectConfEntry>();
        List<DBObject> list = new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("dflg", DeleteState.NORMAL.getState());
        query.append("xkid", xuankeId);
        query.append("type", type);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new SubjectConfEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 删除学科
     *
     * @param id
     */
    public void deleteSubjectConf(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject value = new BasicDBObject("dflg", DeleteState.DELETED.getState());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }


    /**
     * 更新学科
     *
     * @param subjectConfEntry
     * @param id
     */
    public void updateSubjectConf(SubjectConfEntry subjectConfEntry, ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id).append("type", subjectConfEntry.getType());
        DBObject value = new BasicDBObject("subid", subjectConfEntry.getSubjectId())
                .append("advtm", subjectConfEntry.getAdvanceTime())
                .append("siptm", subjectConfEntry.getSimpleTime())
                .append("iffc", subjectConfEntry.getIfFengCeng())
                .append("explain", subjectConfEntry.getExplain());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }


    /**
     * 公示、取消公示
     *
     * @param id
     * @param isPublic
     */
    public void isPublic(ObjectId id, int isPublic) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject value = new BasicDBObject("ispub", isPublic);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }

    /**
     * 发布、取消发布
     *
     * @param id
     * @param isRelease
     */
    public void isRelease(ObjectId id, int isRelease) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject value = new BasicDBObject("isrels", isRelease);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }

    /**
     * 选课记录更新
     *
     * @param xuankeId
     * @param advanceCount
     * @param simpleCount
     * @param startDate
     * @param endDate
     */
    public void updateXuanKeConf(ObjectId xuankeId, int advanceCount, int simpleCount, long startDate, long endDate) {
        DBObject query = new BasicDBObject(Constant.ID, xuankeId);
        DBObject value = new BasicDBObject("advcnt", advanceCount)
                .append("sipcnt", simpleCount)
                .append("stadt", startDate)
                .append("eddt", endDate);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }

    /**
     * 通过选课id删除学科
     *
     * @param xuanKeId
     */
    public void deleteSubjectConfByXuanKeId(String xuanKeId, int type) {
        DBObject query = new BasicDBObject("xkid", new ObjectId(xuanKeId));
        DBObject value = new BasicDBObject("dflg", DeleteState.DELETED.getState()).append("type", type);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }

    /**
     * @param subjectId
     * @param classId
     */
    public List<SubjectConfEntry> findStudentCourseList(String xuanKeId, String subjectId, String classId, int type) {
        List<SubjectConfEntry> retList = new ArrayList<SubjectConfEntry>();
        List<DBObject> list = new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("dflg", DeleteState.NORMAL.getState());
        query.append("subid", new ObjectId(subjectId));
        query.append("xkid", new ObjectId(xuanKeId));
        if (!StringUtils.isEmpty(classId)) {
            BasicDBList basicDBList = new BasicDBList();
            basicDBList.add(new BasicDBObject("ausers.v", new ObjectId(classId)));
            basicDBList.add(new BasicDBObject("susers.v", new ObjectId(classId)));
            query.append(Constant.MONGO_OR, basicDBList);
            /*query.append("ausers.v",new ObjectId(classId));
            query.append("susers.v",new ObjectId(classId));*/
        }
        query.append("type", type);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new SubjectConfEntry((BasicDBObject) dbo));
        }
        return retList;
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
        List<DBObject> list = new ArrayList<DBObject>();

        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        query.append("cid", classId);
        if (!StringUtils.isEmpty(userName)) {
            Pattern pattern = Pattern.compile("^.*" + userName + ".*$", Pattern.MULTILINE);
            query.append("um", new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }

//        if (choose==1) {
//            query.append("isx",0);
//        } else {
        query.append("isx", choose);
//        }

        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new StudentChooseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 选课进度学生数量
     *
     * @param xuanKeId
     * @param classId
     * @param choose
     * @param userName
     * @return
     */
    public int studentXuanKeCount(ObjectId xuanKeId, ObjectId classId, int choose, String userName) {
        List<StudentChooseEntry> retList = new ArrayList<StudentChooseEntry>();
        List<DBObject> list = new ArrayList<DBObject>();

        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        query.append("cid", classId);
        query.append("um", userName);
        if (choose == 1) {
            query.append("isx", 0);
        } else {
            query.append("isx", 1);
        }

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query);

    }

    /**
     * 设置班级人数
     *
     * @param xuanKeId
     * @param max
     * @param min
     */
    public void updateSubjectConfCount(ObjectId xuanKeId, int max, int min) {
        DBObject query = new BasicDBObject("xkid", xuanKeId);
        DBObject value = new BasicDBObject("advmax", max).append("advmin", min).append("sipmax", max).append("sipmin", min);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }

    /**
     * 设置班级人数
     *
     * @param subjectConfId
     * @param type
     * @param choose
     * @param count
     */
    public void updateSubjectConfSingleCount(ObjectId subjectConfId, int type, int choose, int count) {
        DBObject query = new BasicDBObject(Constant.ID, subjectConfId);
        DBObject value = new BasicDBObject();
        if (type == 1) {
            if (choose == 1) {
                value = new BasicDBObject("advmin", count);
            } else {
                value = new BasicDBObject("advmax", count);
            }
        } else {
            if (choose == 1) {
                value = new BasicDBObject("sipmin", count);
            } else {
                value = new BasicDBObject("sipmax", count);
            }
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }

    public List<SubjectConfEntry> findSubjectConfByFenCeng(ObjectId xuanKeId, int type) {
        List<SubjectConfEntry> retList = new ArrayList<SubjectConfEntry>();
        List<DBObject> list = new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("dflg", DeleteState.NORMAL.getState());
        query.append("xkid", xuanKeId);
        query.append("type", type);
        query.append("iffc", 1);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new SubjectConfEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 更新考试id
     *
     * @param xuankeId
     * @param examId
     */
    public void updateXuanKeConfExamId(ObjectId xuankeId, ObjectId examId) {
        DBObject query = new BasicDBObject(Constant.ID, xuankeId);
        DBObject value = new BasicDBObject("exid", examId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }

    /**
     * 根据学科id获取
     *
     * @param xuankeId
     * @param subjectId
     * @return
     */
    public SubjectConfEntry findBySubjectId(ObjectId xuankeId, ObjectId subjectId) {
        BasicDBObject query = new BasicDBObject("dflg", DeleteState.NORMAL.getState());
        query.append("xkid", xuankeId);
        query.append("type", 1);
        query.append("subid", subjectId);
        query.append("dflg", 0);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, Constant.FIELDS);
        if (dbObject != null)
            return new SubjectConfEntry((BasicDBObject) dbObject);
        return null;
    }

    /**
     * 根据班级id获取本班学生选课情况
     *
     * @param xuanKeId
     * @param classId
     * @return
     */
    public List<StudentChooseEntry> findStuXuanKeListByClassId(ObjectId xuanKeId, ObjectId classId) {
        List<StudentChooseEntry> retList = new ArrayList<StudentChooseEntry>();
        List<DBObject> list = new ArrayList<DBObject>();

        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        query.append("cid", classId);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
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
        List<DBObject> list = new ArrayList<DBObject>();

        BasicDBObject query = new BasicDBObject("xkid", xuanKeId);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_CHOOSE, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new StudentChooseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 检查学科是否已存在
     *
     * @param xuanKeId
     * @param subjectId
     * @return
     */
    public int checkSubjectConf(String xuanKeId, String subjectId) {
        BasicDBObject query = new BasicDBObject("xkid", new ObjectId(xuanKeId)).append("subid", new ObjectId(subjectId)).append("dflg", DeleteState.NORMAL.getState());
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query);
    }

    /**
     * 更新分班数量
     *
     * @param xuankeId
     * @param classcnt
     */
    public void updateXuanKeClassCnt(ObjectId xuankeId, int classcnt) {
        DBObject query = new BasicDBObject(Constant.ID, xuankeId);
        DBObject value = new BasicDBObject("clscnt", classcnt);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }

    /**
     * 清除选课数据
     *
     * @param xuankeId
     * @param subjectId
     */
    public void removeXuankeStudent(ObjectId xuankeId, List<ObjectId> subjectId) {
        List<ObjectId> list = new ArrayList<ObjectId>();
        DBObject query = new BasicDBObject("xkid", xuankeId)
                .append("subid", new BasicDBObject(Constant.MONGO_IN, subjectId));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ausers", MongoUtils.convert(list))
                .append("susers", MongoUtils.convert(list)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);

    }
}
