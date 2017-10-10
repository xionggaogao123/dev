package com.pojo.appmarket;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailStarStatisticEntry extends BaseDBObject{

    public AppDetailStarStatisticEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppDetailStarStatisticEntry(ObjectId appDetailId,
                                       int star,
                                       int count){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("apd",appDetailId)
                .append("sr",star)
                .append("cn",count)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public int getCount(){
        return getSimpleIntegerValue("cn");
    }

    public void setCount(int count){
        setSimpleValue("cn",count);
    }

    public void setStar(int star){
        setSimpleValue("sr",star);
    }

    public int getStar(){
        return getSimpleIntegerValue("sr");
    }

    public void setAppDetailId(ObjectId appDetailId){
        setSimpleValue("apd",appDetailId);
    }

    public ObjectId getAppDetailId(){
        return getSimpleObjecIDValue("apd");
    }
}
