package com.pojo.instantmessage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/10/25.
 * id
 * userId      用户id      uid
 * dataTime    时间        dtm
 * newNumber   未读数      nmb
 * type        模块类型    typ
 */
public class RedDotEntry extends BaseDBObject {
    public RedDotEntry(){

    }
    public RedDotEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public RedDotEntry(
            ObjectId userId,
            long dateTime,
            int newNumber,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("dtm", dateTime)
                .append("num", newNumber)
                .append("typ",type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public RedDotEntry(
            ObjectId id,
            ObjectId userId,
            long dateTime,
            int newNumber,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("dtm", dateTime)
                .append("num", newNumber)
                .append("typ",type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }

    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public int getNewNumber(){
        return getSimpleIntegerValue("num");
    }

    public void setNewNumber(int newNumber){
        setSimpleValue("num",newNumber);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
