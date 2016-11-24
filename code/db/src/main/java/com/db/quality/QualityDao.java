package com.db.quality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.Quality.QualityEntry;
import com.pojo.property.PropertyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/10/24.
 */
public class QualityDao extends BaseDao {

    /**
     * 添加一条老师教学质量
     * @param e
     * @return
     */
    public ObjectId addQualityEntry(QualityEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY_INFO, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 跟新老师教学质量
     * @param term
     * @param type
     * @param teacherId
     * @param comment
     * @param score
     */
    public void updateQualityEntry(String term,int type,ObjectId teacherId,String comment,String score) {
        DBObject query =new BasicDBObject("term",term).append("ti",teacherId);
        DBObject updateValue = null;
        if (type==0) {
            updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("com",comment));
        } else {
            updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sc",score));
        }
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY_INFO, query, updateValue);
    }

    /**
     * 查询老师质量
     * @param term
     * @param teacherIds
     * @return
     */
    public List<QualityEntry> selQualityList(String term,List<ObjectId> teacherIds) {
        List<QualityEntry> retList =new ArrayList<QualityEntry>();
        DBObject query =new BasicDBObject("term",term).append("ti",new BasicDBObject(Constant.MONGO_IN,teacherIds));

        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY_INFO, query, Constant.FIELDS);
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new QualityEntry((BasicDBObject)dbo) );
            }
        }
        return retList;
    }

    /**
     * 查询老师一条质量
     * @param term
     * @param teacherId
     * @return
     */
    public QualityEntry selQualityEntry(String term,ObjectId teacherId) {
        DBObject query =new BasicDBObject("term",term).append("ti",teacherId);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return (new QualityEntry((BasicDBObject)dbo) );
        }
        return null;
    }

    /**
     * 查询老师质量
     * @param term
     * @param teacherIds
     * @return
     */
    public List<QualityEntry> selQualityEntryByTeas(String term,List<ObjectId> teacherIds) {
        List<QualityEntry> retList =new ArrayList<QualityEntry>();
        DBObject query =new BasicDBObject("term",term).append("ti",new BasicDBObject(Constant.MONGO_IN,teacherIds));
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY_INFO, query, Constant.FIELDS);
        if(null!=list && list.size()>0)
        {
            for(DBObject dbo:list)
            {
                retList.add(new QualityEntry((BasicDBObject)dbo) );
            }
        }
        return retList;
    }
}
