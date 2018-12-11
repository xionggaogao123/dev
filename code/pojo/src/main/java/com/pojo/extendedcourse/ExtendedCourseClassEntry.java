package com.pojo.extendedcourse;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-12-06.
 *
 * 拓展课 — 课节
 *
 *  schoolId            学校id            sid
 *  courseId            课程id            cid
 *  classList<String>           课节集合          clt    (构造为     日期+课时  如   2018-12-06*1  2018年12月6日第1节课)
 *
 *
 */
public class ExtendedCourseClassEntry extends BaseDBObject {
    public ExtendedCourseClassEntry(){

    }

    public ExtendedCourseClassEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public ExtendedCourseClassEntry(
            ObjectId schoolId,
            ObjectId courseId,
            List<String> classList
    ){

        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("cid", courseId)
                .append("clt", classList)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);

    }
    public List<String> getClassList(){
        @SuppressWarnings("rawtypes")
        List classList =(List)getSimpleObjectValue("clt");
        return classList;
    }

    public void setClassList(List<String> classList){
        setSimpleValue("clt",classList);
    }
    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setCourseId(ObjectId courseId){
        setSimpleValue("cid",courseId);
    }

    public ObjectId getCourseId(){
        return getSimpleObjecIDValue("cid");
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
