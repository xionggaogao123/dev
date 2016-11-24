package com.pojo.overtime;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/14.
 */
public class OverTimeEntry extends BaseDBObject {
    private static final long serialVersionUID = -3877889974404141539L;

    public OverTimeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public OverTimeEntry(ObjectId schoolId,ObjectId applicantUserId,ObjectId jbUserId,long date,String startTime,String endTime,String cause,ObjectId auditUserId,double salary,String content,List<LessonWare> lessonWareList) {
        super();
        List<DBObject> list = MongoUtils.fetchDBObjectList(lessonWareList);
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si",schoolId)
                .append("apuid", applicantUserId)
                .append("juid", jbUserId)
                .append("date",date)
                .append("st",startTime)
                .append("et", endTime)
                .append("it", 0l)
                .append("ot", 0l)
                .append("cause", cause)
                .append("atuid", auditUserId)
                .append("pay",salary)
                .append("con", content)
                .append("dcl", MongoUtils.convert(list))
                .append("type", 0)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public ObjectId getDutyTimeId() {
        return getSimpleObjecIDValue("dtid");
    }
    public void setApplicantUserId(ObjectId applicantUserId) {
        setSimpleValue("apuid",applicantUserId);
    }
    public ObjectId getApplicantUserId() {
        return getSimpleObjecIDValue("apuid");
    }
    public void setJbUserId(ObjectId jbUserId) {
        setSimpleValue("juid",jbUserId);
    }
    public ObjectId getJbUserId() {
        return getSimpleObjecIDValue("juid");
    }
    public void setDate(long date) {
        setSimpleValue("date",date);
    }
    public long getDate() {
        return getSimpleLongValue("date");
    }
    public void setAuditUserId(ObjectId auditUserId) {
        setSimpleValue("atuid",auditUserId);
    }
    public ObjectId getAuditUserId() {
        return getSimpleObjecIDValue("atuid");
    }
    public void setSalary(double salary) {
        setSimpleValue("pay",salary);
    }
    public double getSalary() {
        return getSimpleDoubleValue("pay");
    }
    public void setContent(String content) {
        setSimpleValue("con",content);
    }
    public String getContent() {
        return getSimpleStringValue("con");
    }
    public String getfilePath() {
        return getSimpleStringValue("fpath");
    }
    public void setFilePath(String filePath) {
        setSimpleValue("fpath",filePath);
    }
    public String getRealName() {
        return getSimpleStringValue("rn");
    }
    public void setRealName(String realName) {
        setSimpleValue("rn",realName);
    }
    public void setStartTime(String startTime) {
        setSimpleValue("st",startTime);
    }
    public String getStartTime() {
        return getSimpleStringValue("st");
    }
    public void setEndTime(String endTime) {
        setSimpleValue("et",endTime);
    }
    public String getEndTime() {
        return getSimpleStringValue("et");
    }
    public void setCause(String cause) {
        setSimpleValue("cause",cause);
    }
    public String getCause() {
        return getSimpleStringValue("cause");
    }
    public void setType(int type) {
        setSimpleValue("type",type);
    }
    public int getType() {
        return getSimpleIntegerValue("type");
    }
    public long getOutTime() {
        return getSimpleLongValue("ot");
    }
    public void setOutTime(long outTime) {
        setSimpleValue("ot", outTime);
    }
    public long getInTime() {
        return getSimpleLongValue("it");
    }
    public void setInTime(long inTime) {
        setSimpleValue("it", inTime);
    }
    public List<LessonWare> getLessonWareList() {
        List<LessonWare> lessonWareList =new ArrayList<LessonWare>();

        BasicDBList list =(BasicDBList)getSimpleObjectValue("dcl");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                lessonWareList.add(  new LessonWare((BasicDBObject)o));
            }
        }
        return lessonWareList;
    }

    public List<BasicDBObject> getLessonWareDBOList() {
        List<BasicDBObject> lessonWareList =new ArrayList<BasicDBObject>();
        if(null!=getLessonWareList() && !getLessonWareList().isEmpty())
        {
            for(LessonWare o:getLessonWareList())
            {
                lessonWareList.add(o.getBaseEntry());
            }
        }
        return lessonWareList;
    }

    public void setLessonWareList(List<LessonWare> lessonWareList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(lessonWareList);
        setSimpleValue("dcl",  MongoUtils.convert(list));
    }
}
