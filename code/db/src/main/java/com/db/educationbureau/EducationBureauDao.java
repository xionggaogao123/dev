package com.db.educationbureau;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.examregional.EducationSubject;
import com.pojo.school.Grade;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/5/14.
 */
public class EducationBureauDao extends BaseDao {
    /**
     * 添加教育局
     * @param e
     * @return
     */
    public ObjectId addEducation(EducationBureauEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除教育局
     * @param id
     * @return
     */
    public void delEducation(ObjectId id) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st", DeleteState.DELETED.getState()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 查询全部教育局
     */
    public List<EducationBureauEntry> selAllEducation() {
        List<EducationBureauEntry> retList =new ArrayList<EducationBureauEntry>();
        BasicDBObject query =new BasicDBObject("st", DeleteState.NORMAL.getState());
        List<DBObject> list= find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new EducationBureauEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    /**
     * 修改教育局名称
     * @param id
     * @param educationName
     * @return
     */
    public void updateEducationInfo(ObjectId id, String educationName,
                                    String province,String city,String county,Long updateTime) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("nm",educationName).append("pr",province)
                .append("ci", city)
                .append("co", county)
                .append("upt", updateTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 修改教育局
     * @param entry
     * @return
     */
    public void updateEducationInfo(EducationBureauEntry entry) {
        BasicDBObject query =new BasicDBObject(Constant.ID, entry.getID());
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(entry.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, update);
    }

    /**
     * 增加一个教育局用户
     * @param id
     * @param userId
     */
    public void addEduUser(ObjectId id,ObjectId userId,Long updateTime)
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("uis",userId))
                .append(Constant.MONGO_SET,new BasicDBObject("upt",updateTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 增加一批教育局用户
     * @param id
     * @param uids
     */
    public void addEduUsers(ObjectId id, List<ObjectId> uids,Long updateTime) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject operDBO =new BasicDBObject(Constant.MONGO_EACH,uids);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("uis",operDBO))
                .append(Constant.MONGO_SET,new BasicDBObject("upt",updateTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 删除一个教育局用户
     * @param id
     * @param userId
     */
    public void delEduUser(ObjectId id, ObjectId userId,Long updateTime) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("uis",userId))
                .append(Constant.MONGO_SET,new BasicDBObject("upt",updateTime));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU,query,update);
    }

    /**
     * 增加一个教育局管辖学校
     * @param id
     * @param schoolId
     */
    public void addRelationSchool(ObjectId id,String schoolCreateDate,ObjectId schoolId,Long updateTime)
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("sis",schoolId))
                .append(Constant.MONGO_SET, new BasicDBObject("upt", updateTime).append("scrd", schoolCreateDate));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 增加一批教育局管辖学校
     * @param id
     * @param sids
     */
    public void addRelationSchools(ObjectId id, String schoolCreateDate, List<ObjectId> sids, Long updateTime) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject operDBO =new BasicDBObject(Constant.MONGO_EACH,sids);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("sis",operDBO))
                .append(Constant.MONGO_SET, new BasicDBObject("upt", updateTime).append("scrd",schoolCreateDate));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 删除一个教育局管辖学校
     * @param id
     * @param schoolId
     */
    public void delRelationSchool(ObjectId id, ObjectId schoolId,Long updateTime) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("sis",schoolId))
                .append(Constant.MONGO_SET, new BasicDBObject("upt", updateTime));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU,query,update);
    }

    /**
     * 通过教育局id查询教育局
     * @param id
     */
    public EducationBureauEntry selEducationById(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new EducationBureauEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 通过userId查询教育局
     * @param userId
     */
    public EducationBureauEntry selEducationByUserId(ObjectId userId) {
        BasicDBObject query =new BasicDBObject("uis",userId);
        query.append("st", DeleteState.NORMAL.getState());
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new EducationBureauEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 通过schoolId查询教育局
     * @param schoolId
     */
    public List<EducationBureauEntry> selEducationBySchoolId(ObjectId schoolId) {
        List<EducationBureauEntry> retList =new ArrayList<EducationBureauEntry>();
        BasicDBObject query =new BasicDBObject("sis",schoolId);
        query.append("st", DeleteState.NORMAL.getState());
        List<DBObject> list= find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new EducationBureauEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 通过educationName查询教育局信息
     * @param educationName
     */
    public EducationBureauEntry selEducationByEducationName(String educationName) {
        BasicDBObject query =new BasicDBObject("nm",educationName);
        query.append("st", DeleteState.NORMAL.getState());
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new EducationBureauEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 通过uids查询教育局信息集合
     * @param uids
     */
    public List<EducationBureauEntry> selEducationByUserIds(List<ObjectId> uids) {
        List<EducationBureauEntry> retList =new ArrayList<EducationBureauEntry>();
        BasicDBObject query =new BasicDBObject("uis",new BasicDBObject(Constant.MONGO_IN,uids));
        query.append("st", DeleteState.NORMAL.getState());
        List<DBObject> list= find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new EducationBureauEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public EducationBureauEntry selEducationByRegionId(String regionId) {
        BasicDBObject query =new BasicDBObject();
        query.append("st", DeleteState.NORMAL.getState());
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("pr",regionId));
        dblist.add(new BasicDBObject("ci",regionId));
        dblist.add(new BasicDBObject("co",regionId));
        query.append(Constant.MONGO_OR,dblist);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new EducationBureauEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 增加一个年级
     * @param eduId
     * @param grade
     */
    public void addGrade(ObjectId eduId, Grade grade){
        BasicDBObject query = new BasicDBObject(Constant.ID, eduId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("grs",grade.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 编辑年级
     * @param eduId
     * @param gradeName
     * @param gradeType
     */
    public void editGrade(ObjectId eduId, ObjectId gradeId, String gradeName, int gradeType){
        BasicDBObject query = new BasicDBObject(Constant.ID, eduId).append("grs.gid", gradeId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("grs.$.nm",gradeName).append("grs.$.ty", gradeType));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 删除年级
     * @param eduId
     * @param gradeId
     */
    public void delGrade(ObjectId eduId, ObjectId gradeId){
        BasicDBObject query = new BasicDBObject(Constant.ID, eduId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("grs",new BasicDBObject("gid", gradeId)));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 增加一个学科
     * @param eduId
     * @param subject
     */
    public void addSubject(ObjectId eduId, EducationSubject subject){
        BasicDBObject query = new BasicDBObject(Constant.ID, eduId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("subs",subject.getBaseEntry()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 编辑学科
     * @param eduId
     * @param subjectName
     * @param subjectType
     */
    public void editSubject(ObjectId eduId,ObjectId subjectId, String subjectName, int subjectType, List<ObjectId> gradeList){
        BasicDBObject query = new BasicDBObject(Constant.ID, eduId).append("subs.si", subjectId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("subs.$.nm",subjectName).append("subs.$.ty", subjectType).append("subs.$.gis", gradeList));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    /**
     * 删除学科
     * @param eduId
     * @param subjectId
     */
    public void delSubject(ObjectId eduId, ObjectId subjectId){
        BasicDBObject query = new BasicDBObject(Constant.ID, eduId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("subs",new BasicDBObject("si", subjectId)));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU, query, updateValue);
    }

    public int selEducationCount(String eduName, ObjectId province, ObjectId city, ObjectId county) {
        BasicDBObject query =new BasicDBObject();
        if(StringUtils.isNotBlank(eduName))
        {
            query.append("nm", MongoUtils.buildRegex(eduName));
        }
        if(province!=null){
            query.append("pr",province.toString());
        }
        if(city!=null){
            query.append("ci",city.toString());
        }
        if(county!=null){
            query.append("co",county.toString());
        }
        query.append("st", DeleteState.NORMAL.getState());
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_EDUCATION_BUREAU,query);
    }

    public List<EducationBureauEntry> selEducationList(String eduName, ObjectId province, ObjectId city, ObjectId county, int skip, int limit) {
        List<EducationBureauEntry> retList =new ArrayList<EducationBureauEntry>();
        BasicDBObject query =new BasicDBObject();
        if(StringUtils.isNotBlank(eduName))
        {
            query.append("nm", MongoUtils.buildRegex(eduName));
        }
        if(province!=null){
            query.append("pr",province.toString());
        }
        if(city!=null){
            query.append("ci",city.toString());
        }
        if(county!=null){
            query.append("co",county.toString());
        }
        query.append("st", DeleteState.NORMAL.getState());
        List<DBObject> list =new ArrayList<DBObject>();
        if(skip>=0 && limit>0)
        {
            list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS,Constant.MONGO_SORTBY_DESC, skip, limit);
        }
        else
        {
            list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        }

        for(DBObject dbo:list)
        {
            retList.add(new EducationBureauEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    public List<EducationBureauEntry> getEducationListByParams(String eduName, List<ObjectId> noeduIds) {
        List<EducationBureauEntry> retList =new ArrayList<EducationBureauEntry>();
        BasicDBObject query =new BasicDBObject();
        query.append("st", DeleteState.NORMAL.getState());
        if(noeduIds.size()>0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, noeduIds));
        }
        if(StringUtils.isNotBlank(eduName))
        {
            query.append("nm", MongoUtils.buildRegex(eduName));
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU,query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new EducationBureauEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<EducationBureauEntry> findEduInfoByEduIds(List<ObjectId> eduIds) {
        List<EducationBureauEntry> retList =new ArrayList<EducationBureauEntry>();
        BasicDBObject query =new BasicDBObject();
        query.append("st", DeleteState.NORMAL.getState());
        if(eduIds.size()>0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, eduIds));
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION_BUREAU,query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new EducationBureauEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
}
