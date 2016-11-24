package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/11/9.
 * {
 *     走班通知biao
 *     _id
 *     te:------------------->term
 *     sid:------------------->schoolId
 *     gid:------------------>gradeId
 *     nm:------------------->name 通知名字
 *     ty:------------------->type 0:周内调课  1跨周调课   2长期调课
 *     des-------------------->descript 说明
 *     week:----------------->week 调整周数
 *     dt-------------------->time 时间
 *     con:------------------->content 详细内容
 *     ti:-------------------->tableid
 *     cl:-------------------->classIdList
 *     df--------------------->deleteFlag 0未删除  1已删除
 * }
 */
public class ZoubanNotice extends BaseDBObject{
    public ZoubanNotice()
    {
        super();
    }
    public ZoubanNotice(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public ZoubanNotice(String term,ObjectId schoolId,ObjectId gradeId,String name,int type,String descript,String week,String time,
                        List<NoticeDetail> noticeDetails,List<ObjectId> tableIds,List<ObjectId> classIds,int deleteFlag)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("te",term)
                .append("sid",schoolId)
                .append("gid",gradeId)
                .append("nm", name)
                .append("ty",type)
                .append("des",descript)
                .append("week", week)
                .append("dt",time)
                .append("con",MongoUtils.convert(MongoUtils.fetchDBObjectList(noticeDetails)))
                .append("ti",MongoUtils.convert(tableIds))
                .append("cl",MongoUtils.convert(classIds))
                .append("df", deleteFlag);
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

    public ObjectId getSchoolId()
    {
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getGradeId()
    {
        return getSimpleObjecIDValue("gid");
    }
    public void setGradeId(ObjectId gradeId)
    {
        setSimpleValue("gid",gradeId);
    }

    public String getName()
    {
        return getSimpleStringValue("nm");
    }
    public void setName(String name)
    {
        setSimpleValue("nm",name);
    }

    public int getType()
    {
        return getSimpleIntegerValue("ty");
    }
    public void setType(int type)
    {
        setSimpleValue("ty",type);
    }

    public String getWeek()
    {
        return getSimpleStringValue("week");
    }
    public void setWeek(String week)
    {
        setSimpleValue("week",week);
    }

    public String getTime()
    {
        return getSimpleStringValue("dt");
    }
    public void setTime(String time)
    {
        setSimpleValue("dt",time);
    }

    public String getDes()
    {
        return getSimpleStringValue("des");
    }
    public void setDes(String des)
    {
        setSimpleValue("des",des);
    }

    public List<NoticeDetail> getNoticeDetail()
    {
        List<NoticeDetail> noticeDetails=new ArrayList<NoticeDetail>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("con");
        if (basicDBList!=null && !basicDBList.isEmpty())
        {
            for (Object o : basicDBList) {
                noticeDetails.add(new NoticeDetail((BasicDBObject)o));
            }
        }
        return noticeDetails;
    }
    public void setNoticeDetail(List<NoticeDetail> noticeDetailList)
    {
        setSimpleValue("con", MongoUtils.convert(MongoUtils.fetchDBObjectList(noticeDetailList)));
    }
    public List<ObjectId> getTableId()
    {
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("ti");
        if(basicDBList!=null && !basicDBList.isEmpty())
        {
            for (Object o:basicDBList)
            {
                objectIdList.add((ObjectId)o);
            }
        }
        return objectIdList;
    }
    public void setTableId(List<ObjectId> tableIds)
    {
        setSimpleValue("ti",MongoUtils.convert(tableIds));
    }
    public List<ObjectId> getClassId()
    {

        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        if(this.getBaseEntry().containsField("cl")) {
            BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("cl");
            if (basicDBList != null && !basicDBList.isEmpty()) {
                for (Object o : basicDBList) {
                    objectIdList.add((ObjectId) o);
                }
            }
        }
        return objectIdList;
    }
    public void setClassId(List<ObjectId> classIds)
    {
        setSimpleValue("cl",MongoUtils.convert(classIds));
    }
    public int getDeleteFlag()
    {
        return getSimpleIntegerValue("df");
    }
    public void setDeleteFlag(int deleteFlag)
    {
        setSimpleValue("df",deleteFlag);
    }
}
