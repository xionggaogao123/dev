package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/10/13.
 */
public class ExamTypeEntry extends BaseDBObject{

    public ExamTypeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ExamTypeEntry(String examTypeName){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("et",examTypeName)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setExamTypeName(String examTypeName){
        setSimpleValue("et",examTypeName);
    }

    public String getExamTypeName(){
        return getSimpleStringValue("et");
    }
}
