package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * Created by qiangm on 2015/11/9.
 * {
 *     走班通知详情
 *     id：---------------------->id
 *     cl------------------------>className
 *     co:----------------------->course
 *     te:----------------------->teacher
 *     ot------------------------>原上课时间
 *     nt------------------------>新上课时间
 * }
 */
public class NoticeDetail extends BaseDBObject {
    public NoticeDetail()
    {
        super();
    }
    public NoticeDetail(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public NoticeDetail(ObjectId id,String className,String courseName,String teacherName,String oldTime,
                        String newTime)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("id",id)
                .append("cl",className)
                .append("co",courseName)
                .append("te",teacherName)
                .append("ot",oldTime)
                .append("nt",newTime);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }
    public void setId(ObjectId id)
    {
        setSimpleValue("id",id);
    }

    public String getClassName()
    {
        return getSimpleStringValue("cl");
    }
    public void setClassName(String className)
    {
        setSimpleValue("cl",className);
    }

    public String getCourseName()
    {
        return getSimpleStringValue("co");
    }
    public void setCourseName(String courseName)
    {
        setSimpleValue("co",courseName);
    }

    public String getTeacherName()
    {
        return getSimpleStringValue("te");
    }
    public void setTeacherName(String teacherName)
    {
        setSimpleValue("te",teacherName);
    }

    public String getOldTime()
    {
        return getSimpleStringValue("ot");
    }
    public void setOldTime(String oldTime)
    {
        setSimpleValue("ot",oldTime);
    }

    public String getNewTime()
    {
        return getSimpleStringValue("nt");
    }
    public void setNewTime(String newTime)
    {
        setSimpleValue("nt",newTime);
    }
}
