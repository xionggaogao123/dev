package com.pojo.leave;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 代课表
 * _id
 * sid:schoolId
 * tid:老师id
 * cid:courseId
 * co:course
 * wk:week
 * x:x 星期
 * y:y 第几节
 * otid:oldTeacherId
 * mid:managerId
 * te:term
 * li:leaveId
 * dl:delete
 *
 * Created by qiangm on 2016/3/4.
 */
public class ReplaceCourse extends BaseDBObject {
    public ReplaceCourse()
    {
        super();
    }
    public ReplaceCourse(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public ReplaceCourse(ObjectId schoolId,ObjectId teacherId,ObjectId courseId,
                         String courseName,int week,int x,int y,
                         ObjectId oldTeacherId,
                         ObjectId managerId,String term,ObjectId leaveId)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sid",schoolId)
                .append("tid",teacherId)
                .append("cid",courseId)
                .append("co",courseName)
                .append("wk", week)
                .append("x",x)
                .append("y",y)
                .append("otid",oldTeacherId)
                .append("mid",managerId)
                .append("te",term)
                .append("li",leaveId)
                .append("dl",0);
        setBaseEntry(basicDBObject);
    }

    public void setSchoolId(ObjectId schoolId)
    {
        setSimpleValue("sid",schoolId);
    }
    public ObjectId getSchoolId()
    {
        return getSimpleObjecIDValue("sid");
    }

    public void setTeacherId(ObjectId teacherId)
    {
        setSimpleValue("tid",teacherId);
    }
    public ObjectId getTeacherId()
    {
        return getSimpleObjecIDValue("tid");
    }
    public void setCourseId(ObjectId courseId)
    {
        setSimpleValue("cid",courseId);
    }
    public ObjectId getCourseId()
    {
        return getSimpleObjecIDValue("cid");
    }
    public void setCourseName(String courseName)
    {
        setSimpleValue("co",courseName);
    }
    public String getCourseName()
    {
        return getSimpleStringValue("co");
    }

    public void setWeek(int week)
    {
        setSimpleValue("wk",week);
    }
    public int getWeek()
    {
        return getSimpleIntegerValue("wk");
    }

    public void setX(int x)
    {
        setSimpleValue("x",x);
    }
    public int getX()
    {
        return getSimpleIntegerValue("x");
    }

    public void setY(int y)
    {
        setSimpleValue("y",y);
    }
    public int getY()
    {
        return getSimpleIntegerValue("y");
    }

    public void setOldTeacherId(ObjectId oldTeacherId)
    {
        setSimpleValue("otid",oldTeacherId);
    }
    public ObjectId getOldTeacherId()
    {
        return getSimpleObjecIDValue("otid");
    }

    public void setManagerId(ObjectId managerId)
    {
        setSimpleValue("mid",managerId);
    }
    public ObjectId getManagerId()
    {
        return getSimpleObjecIDValue("mid");
    }
    public void setTerm(String term)
    {
        setSimpleValue("te",term);
    }
    public String getTerm()
    {
        return getSimpleStringValue("te");
    }
    public void setLeaveId(ObjectId leaveId)
    {
        setSimpleValue("li",leaveId);
    }
    public ObjectId getLeaveId()
    {
        if(this.getBaseEntry().containsField("li"))
            return getSimpleObjecIDValue("li");
        return new ObjectId();
    }
    public void setDelete(int delete)
    {
        setSimpleValue("dl", delete);
    }
    public int getDelete()
    {
        if(!this.getBaseEntry().containsField("dl"))
            return 0;
        return getSimpleIntegerValue("dl");
    }
}
