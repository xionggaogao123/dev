package com.pojo.smalllesson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/9/29.
 */
public class SmallLessonCodeEntry extends BaseDBObject{

    public SmallLessonCodeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public SmallLessonCodeEntry(String code){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("co",code)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setCode(String code){
        setSimpleValue("co",code);
    }

    public String getCode(){
        return getSimpleStringValue("co");
    }
}
