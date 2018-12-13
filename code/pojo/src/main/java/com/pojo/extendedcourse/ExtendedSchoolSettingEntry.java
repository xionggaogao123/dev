package com.pojo.extendedcourse;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-12-06.
 *
 * 拓展课 — 学校设置
 *
 * schoolId           学校id               sid
 * courseType         课程类型             cty    1 抢课（实时显示）    2 火速报名（结束显示）
 *
 *
 */
public class ExtendedSchoolSettingEntry extends BaseDBObject {
    public ExtendedSchoolSettingEntry(){

    }

    public ExtendedSchoolSettingEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public ExtendedSchoolSettingEntry(
            ObjectId schoolId,
            int courseType
    ){

        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("cty", courseType)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);

    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setCourseType(int courseType){
        setSimpleValue("cty",courseType);
    }

    public int getCourseType(){
        return getSimpleIntegerValueDef("cty",1);
    }


}
