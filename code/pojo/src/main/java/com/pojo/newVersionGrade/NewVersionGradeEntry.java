package com.pojo.newVersionGrade;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/5.
 */
public class NewVersionGradeEntry extends BaseDBObject{

    public NewVersionGradeEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public NewVersionGradeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public NewVersionGradeEntry(ObjectId userId,
                                String year,
                                int gradeType){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("ye",year)
                .append("gt",gradeType)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }
    public NewVersionGradeEntry(ObjectId id,
                                ObjectId userId,
                                String year,
                                int gradeType){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("ye",year)
                .append("gt",gradeType)
                .append("isr", Constant.ZERO);
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
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
