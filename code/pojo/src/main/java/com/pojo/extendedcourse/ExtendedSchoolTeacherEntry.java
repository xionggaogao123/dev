package com.pojo.extendedcourse;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-12-18.
 *
 *
 * 拓展课 — 课程标签
 *
 * schoolId            学校id              sid
 * name                老师名              nm
 * userId              真实人              uid
 * createId            创建者              cid
 *
 *
 */
public class ExtendedSchoolTeacherEntry extends BaseDBObject {

    public ExtendedSchoolTeacherEntry(){

    }

    public ExtendedSchoolTeacherEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public ExtendedSchoolTeacherEntry(
            ObjectId schoolId,
            String name,
            ObjectId userId,
            ObjectId createId
    ){

        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("nm", name)
                .append("uid", userId)
                .append("cid", createId)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);

    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setName(String name){
        setSimpleValue("nm",name);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setCreateId(ObjectId createId){
        setSimpleValue("cid",createId);
    }

    public ObjectId getCreateId(){
        return getSimpleObjecIDValue("cid");
    }
}
