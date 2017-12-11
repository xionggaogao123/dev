package com.pojo.appnotice;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/12/11.
 */
public class GenerateUserCodeEntry extends BaseDBObject{

    public GenerateUserCodeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public GenerateUserCodeEntry(long seqId){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sei",seqId)
                .append("rn",Math.random())
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public int getRemove(){
        return getSimpleIntegerValue("ir");
    }

    public void setRemove(int remove){
        setSimpleValue("ir",remove);
    }

    public void setSeqId(long seqId){
        setSimpleValue("sei",seqId);
    }

    public long getSeqId(){
        return getSimpleIntegerValue("sei");
    }

    public void setRandom(double random){
        setSimpleValue("rn",random);
    }

    public double getRandom(){
        return getSimpleDoubleValue("rn");
    }
}
