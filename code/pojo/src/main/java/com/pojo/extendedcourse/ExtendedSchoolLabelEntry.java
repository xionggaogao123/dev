package com.pojo.extendedcourse;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-12-06.
 *
 * 拓展课 — 课程标签
 *
 * schoolId            学校id              sid
 * name                标签名              nm
 * userId              创建者              uid
 *
 */
public class ExtendedSchoolLabelEntry extends BaseDBObject {
    public ExtendedSchoolLabelEntry(){

    }

    public ExtendedSchoolLabelEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public ExtendedSchoolLabelEntry(
            ObjectId schoolId,
            ObjectId userId,
            String name
    ){

        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("uid",userId)
                .append("nm", name)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);

    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
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


}
