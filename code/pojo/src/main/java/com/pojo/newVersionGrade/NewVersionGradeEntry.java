package com.pojo.newVersionGrade;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/5.
 */
public class NewVersionGradeEntry extends BaseDBObject{

    public NewVersionGradeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public NewVersionGradeEntry(ObjectId userId,
                                String year,
                                int gradeType){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("ye",year)
                .append("gt",gradeType)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setGradeType(int gradeType){
        setSimpleValue("gt",gradeType);
    }

    public int getGradeType(){
        return getSimpleIntegerValue("gt");
    }

    public String getYear(){
        return getSimpleStringValue("ye");
    }

    public void setYear(String year){
        setSimpleValue("ye",year);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

}
