package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 走班冲突记录表
 * {
 *     te:term-------------->学期
 *     gid:gradeId---------->年级id
 *     did:duanId--------->段id
 *     cid:courseId--------->课程id
 *     cc:conflictCount------>冲突数
 *     cdi:conflictDetailIn----->段内冲突详情，嵌套ConflictDetail
 *     cdo:conflictDetailOut----->段外冲突数
 * }
 * Created by qiangm on 2015/10/15.
 */
public class ZoubanConflict extends BaseDBObject{
    public ZoubanConflict()
    {
        super();
    }
    public ZoubanConflict(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }

    public ZoubanConflict(String term,ObjectId gradeId,ObjectId duanId,ObjectId courseId,int conflictCount,List<ConflictDetail> conflictDetails1,
                          List<ConflictDetail> conflictDetails2)
    {
        BasicDBObject basicDBObject=new BasicDBObject();
        basicDBObject.append("te",term)
                .append("gid",gradeId)
                .append("did",duanId)
                .append("cid", courseId)
                .append("cc",conflictCount)
                .append("cdi",MongoUtils.convert(MongoUtils.fetchDBObjectList(conflictDetails1)))
                .append("cdo", MongoUtils.convert(MongoUtils.fetchDBObjectList(conflictDetails2)));
        setBaseEntry(basicDBObject);
    }
    public String getTerm()
    {
        return getSimpleStringValue("te");
    }
    public void setTerm(String term)
    {
        setSimpleValue("te",term);
    }

    public ObjectId getGradeId()
    {
        return getSimpleObjecIDValue("gid");
    }
    public void setGradeId(ObjectId gradeId)
    {
        setSimpleValue("gid",gradeId);
    }

    public ObjectId getDuanId()
    {
        return getSimpleObjecIDValue("did");
    }
    public void setDuanId(ObjectId DuanId)
    {
        setSimpleValue("did",DuanId);
    }

    public ObjectId getCourseId()
    {
        return getSimpleObjecIDValue("cid");
    }
    public void setCourseId(ObjectId courseId)
    {
        setSimpleValue("cid",courseId);
    }

    public int getConflictCount()
    {
        return getSimpleIntegerValue("cc");
    }
    public void setConflictCount(int count)
    {
        setSimpleValue("cc",count);
    }

    public List<ConflictDetail> getConflictDetailIn()
    {
        List<ConflictDetail> conflictDetails=new ArrayList<ConflictDetail>();
        BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("cdi");
        if(basicDBList!=null &&!basicDBList.isEmpty())
        {
            for (Object object:basicDBList)
            {
                conflictDetails.add(new ConflictDetail((BasicDBObject)object));
            }
        }
        return conflictDetails;
    }
    public void setConflictDetailIn(List<ConflictDetail> conflictDetail)
    {
        setSimpleValue("cdi", MongoUtils.convert(MongoUtils.fetchDBObjectList(conflictDetail)));
    }
    public List<ConflictDetail> getConflictDetailOut()
    {
        List<ConflictDetail> conflictDetails=new ArrayList<ConflictDetail>();
        BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("cdo");
        if(basicDBList!=null &&!basicDBList.isEmpty())
        {
            for (Object object:basicDBList)
            {
                conflictDetails.add(new ConflictDetail((BasicDBObject)object));
            }
        }
        return conflictDetails;
    }
    public void setConflictDetailOut(List<ConflictDetail> conflictDetail)
    {
        setSimpleValue("cdo", MongoUtils.convert(MongoUtils.fetchDBObjectList(conflictDetail)));
    }
}
