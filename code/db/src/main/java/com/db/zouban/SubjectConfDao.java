package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.SubjectConfEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/6/27.
 */
public class SubjectConfDao extends BaseDao {
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
     * 删除配置
     *
     * @param id
     */
    public void deleteSubjectConf(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query);
    }


    /**
     * 更新学科课时
     *
     */
    public void updateSubjectConf(ObjectId id, int advTime, int simTime) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("advtm", advTime).append("siptm", simTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, update);
    }


    /**
     * 清除选课数据
     *
     * @param xuankeId
     * @param subjectId
     */
    public void removeXuankeStudent(ObjectId xuankeId, List<ObjectId> subjectId) {
        DBObject query = new BasicDBObject("xkid", xuankeId)
                .append("subid", new BasicDBObject(Constant.MONGO_IN, subjectId))
                .append("type", 1);

        List<ObjectId> list = new ArrayList<ObjectId>();
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ausers", MongoUtils.convert(list))
                .append("susers", MongoUtils.convert(list)));
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
        IdValuePair idValuePair = new IdValuePair(stuId, classId);
        try {
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("ausers", idValuePair.getBaseEntry())));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("susers", idValuePair.getBaseEntry())));
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * 添加学生
     *
     * @param courseIds
     * @param stuId
     * @param classId
     * @param xuankeId
     * @param type      1 : 合格考 2 : 等级考
     */
    public void addStudents(List<ObjectId> courseIds, ObjectId stuId, ObjectId classId, ObjectId xuankeId, int type) {
        BasicDBObject query = new BasicDBObject("subid", new BasicDBObject(Constant.MONGO_IN, courseIds));
        query.append("xkid", xuankeId);

        IdValuePair idValuePair = new IdValuePair(stuId, classId);
        BasicDBObject updateValue = new BasicDBObject();
        String key = "";
        if (type == 1) {
            key = "susers";
        } else {
            key = "ausers";
        }
        updateValue.append(Constant.MONGO_PUSH, new BasicDBObject(key, idValuePair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, updateValue);
    }

    /**
     * 根据id查询学科配置
     *
     * @param subjectConfId
     * @return
     */
    public SubjectConfEntry findSubjectConfEntry(ObjectId subjectConfId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, subjectConfId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, null);
        if (null != dbo) {
            return new SubjectConfEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据选课id查询学科配置
     *
     * @param xuankeId
     * @return
     */
    public List<SubjectConfEntry> findSubjectConf(ObjectId xuankeId, int type) {
        List<SubjectConfEntry> retList = new ArrayList<SubjectConfEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        query.append("type", type);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, null, Constant.MONGO_SORTBY_ASC);
        if(null!=list&&!list.isEmpty()){
            for (DBObject dbo : list) {
                retList.add(new SubjectConfEntry((BasicDBObject) dbo));
            }
        }

        return retList;
    }

    /**
     * 根据课程type和学科id查询
     * @param xuankeId
     * @param type
     * @param subjectIds
     * @return
     */
    public List<SubjectConfEntry> findSubjectConfList(ObjectId xuankeId, int type, List<ObjectId> subjectIds) {
        BasicDBObject query = new BasicDBObject("xkid", xuankeId)
                .append("type", type)
                .append("subid", new BasicDBObject(Constant.MONGO_IN, subjectIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, Constant.FIELDS);
        List<SubjectConfEntry> subjectConfEntryList = new ArrayList<SubjectConfEntry>();
        for (DBObject dbObject : list) {
            subjectConfEntryList.add(new SubjectConfEntry((BasicDBObject) dbObject));
        }
        return subjectConfEntryList;
    }


    /**
     *
     * @param xuanKeId
     * @param subjectId
     * @param classId
     * @param type
     * @return
     */
    public List<SubjectConfEntry> findStudentCourseList(String xuanKeId, String subjectId, String classId, int type) {
        List<SubjectConfEntry> retList = new ArrayList<SubjectConfEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("subid", new ObjectId(subjectId));
        query.append("xkid", new ObjectId(xuanKeId));
        if (!StringUtils.isEmpty(classId)) {
            BasicDBList basicDBList = new BasicDBList();
            basicDBList.add(new BasicDBObject("ausers.v", new ObjectId(classId)));
            basicDBList.add(new BasicDBObject("susers.v", new ObjectId(classId)));
            query.append(Constant.MONGO_OR, basicDBList);
        }
        query.append("type", type);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query, null, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbo : list) {
            retList.add(new SubjectConfEntry((BasicDBObject) dbo));
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
    public int checkSubjectConf(String xuanKeId, String subjectId,int type) {
        BasicDBObject query = new BasicDBObject("xkid", new ObjectId(xuanKeId))
                .append("subid", new ObjectId(subjectId)).append("type",type);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CONF, query);
    }






}
