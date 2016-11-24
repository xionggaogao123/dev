package com.pojo.attendance;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/7/18.
 */
public class AttendanceEntry extends BaseDBObject {
    /**
     * studentId 学生Id ->sid
     * classId 班级Id ->cid
     * date 日期 ->dt
     * time 时间 ->ti //0全天  1 上午 2下午
     * remark 备注 ->rm
     */

    public AttendanceEntry()
    {
        BasicDBObject basicDBObject=new BasicDBObject();
        basicDBObject.append("sid",null)
                .append("cid",null)
                .append("dt", "")
                .append("ti",0)
                .append("rm","");
    }
    public AttendanceEntry(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public void setStudentId(ObjectId studentId)
    {
        setSimpleValue("sid",studentId);
    }
    public void setClassId(ObjectId classId)
    {
        setSimpleValue("cid",classId);
    }
    public void setDate(String date)
    {
        setSimpleValue("dt",date);
    }
    public void setTime(int time)
    {
        setSimpleValue("ti",time);
    }
    public void setRemark(String remark)
    {
        setSimpleValue("rm",remark);
    }
    public ObjectId getStudentId()
    {
        return getSimpleObjecIDValue("sid");
    }
    public ObjectId getClassId()
    {
        return getSimpleObjecIDValue("cid");
    }
    public String getDate()
    {
        return getSimpleStringValue("dt");
    }
    public int getTime()
    {
        return getSimpleIntegerValue("ti");
    }
    public String getRemark()
    {
        return getSimpleStringValue("rm");
    }
}
