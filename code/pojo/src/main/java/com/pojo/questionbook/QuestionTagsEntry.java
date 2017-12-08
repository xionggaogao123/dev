package com.pojo.questionbook;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/12/8.
 * id                id
 * name             标签名                nam
 * userId           用户id                uid
 *
 *
 *
 */
public class QuestionTagsEntry extends BaseDBObject {
    public QuestionTagsEntry(){

    }

    public QuestionTagsEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public QuestionTagsEntry(
            ObjectId userId,
            String name
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("nam", name)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public QuestionTagsEntry(
            ObjectId id,
            ObjectId userId,
            String name
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("nam", name)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getName(){
        return getSimpleStringValue("nam");
    }

    public void setName(String name){
        setSimpleValue("nam",name);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
