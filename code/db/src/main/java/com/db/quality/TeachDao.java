package com.db.quality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.Quality.TeachCheckEntry;
import com.pojo.Quality.TeacherPlainEntry;
import com.pojo.docflow.IdUserFilePair;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/11/10.
 */
public class TeachDao extends BaseDao {

    /**
     * 添加一条老师教学计划
     * @param e
     * @return
     */
    public ObjectId addTeacherPlainEntry(TeacherPlainEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PLAIN_INFO, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 添加一条老师检查统计
     * @param e
     * @return
     */
    public ObjectId addTeachCheckEntry(TeachCheckEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACH_CHECK_INFO, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除老师教学计划
     * @param id
     * @param ui
     */
    public void delTeacherPlainEntry(ObjectId id,ObjectId ui)
    {
        DBObject query =new BasicDBObject(Constant.ID,id).append("tid", ui);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PLAIN_INFO, query,updateValue);
    }

    /**
     * 查询老师一条教学计划
     * @param id
     * @return
     */
    public TeacherPlainEntry selTeacherPlainEntry(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PLAIN_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return (new TeacherPlainEntry((BasicDBObject)dbo) );
        }
        return null;
    }

    /**
     * 查询老师计划
     * @param term
     * @param userId
     * @param plainName
     * @return
     */
    public List<TeacherPlainEntry> selTeacherPlainEntryList(String term,ObjectId userId,String plainName) {
        List<TeacherPlainEntry> retList =new ArrayList<TeacherPlainEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO).append("term",term);
        if (userId!=null) {
            query.append("tid",userId);
        }
        if (!StringUtils.isEmpty(plainName)) {
            query.append("teanm", MongoUtils.buildRegex(plainName));
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_PLAIN_INFO, query, Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new TeacherPlainEntry((BasicDBObject)dbo) );
            }
        }
        return retList;
    }

    /**
     * 更新教学计划
     * @param id
     * @param entry
     */
    public void updateTeacherPlainEntry(ObjectId id,TeacherPlainEntry entry) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject update =new BasicDBObject("teanm",entry.getTeachName()).append("con",entry.getContent()).append("term",entry.getTerm());
        List<BasicDBObject> fileList = new ArrayList<BasicDBObject>();
        if (entry.getDocFile()!=null && entry.getDocFile().size()!=0) {
            for (IdUserFilePair pair : entry.getDocFile()) {
                fileList.add(pair.getBaseEntry());
            }
            update.append("dcl",fileList);
        }
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PLAIN_INFO, query,updateValue);
    }


    /**
     * 查询老师检查表
     * @param term
     * @param subjectId
     * @param userId
     * @return
     */
    public TeachCheckEntry selTeachCheckEntry(String term, ObjectId subjectId, ObjectId userId) {
        DBObject query =new BasicDBObject("term",term).append("tid",userId).append("sjid",subjectId).append("ir", Constant.ZERO);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACH_CHECK_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return (new TeachCheckEntry((BasicDBObject)dbo) );
        }
        return null;
    }

    /**
     * 查询检查数量
     * @param term
     * @param subjectId
     * @param userId
     * @return
     */
    public int selTeachCheckCount(String term, ObjectId subjectId, ObjectId userId) {
        DBObject query =new BasicDBObject("term",term).append("tid",userId).append("sjid",subjectId).append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACH_CHECK_INFO, query);
    }

    /**
     * 更新评分项的值
     * @param id
     * @param value
     * @param type
     */
    public void updateProjectValue(ObjectId id, String value, int type,String projectName) {
        DBObject query =new BasicDBObject(Constant.ID,id).append("tps.pnm", projectName);

        BasicDBObject setValue = new BasicDBObject("ct", System.currentTimeMillis());
        if (type==1) {
            setValue.append("tps.$.cnt",Integer.valueOf(value));
        } else if (type==2) {
            setValue.append("tps.$.qty",value);
        } else if (type==3) {
            setValue.append("tps.$.sc",value);
        }
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,setValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACH_CHECK_INFO, query,updateValue);
    }
}
