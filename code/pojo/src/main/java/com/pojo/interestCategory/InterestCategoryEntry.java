package com.pojo.interestCategory;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by fl on 2015/11/17.
 * id:
 * sid:学校id
 * nm:名称
 * iq:是否每次必选  0：选修 1：必修  默认0
 * min:整个选课阶段至少选的次数 默认1
 */
public class InterestCategoryEntry extends BaseDBObject{
    public InterestCategoryEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public InterestCategoryEntry(){}

    public InterestCategoryEntry(ObjectId schoolId, String name){
        BasicDBObject dbObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("nm", name)
                .append("iq", 0)
                .append("min", 1);
        setBaseEntry(dbObject);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid", schoolId);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm", name);
    }

    public int getIsRequired(){
        return getSimpleIntegerValue("iq");
    }

    public void setIsRequired(int isRequired){
        setSimpleValue("iq", isRequired);
    }

    public int getMin(){
        return getSimpleIntegerValue("min");
    }

    public void setMin(int min){
        setSimpleValue("min", min);
    }
}
