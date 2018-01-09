package com.pojo.questionbook;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/1/8.
 * id
 * userId             用户id                    uid
 * parentId           家长id/老师id             pid
 * type               家（1）、社区（2）        typ
 * unReadNum          未读数                    unr
 * isRemove           是否删除                  isr
 *
 */
public class QuestionReadEntry extends BaseDBObject {
    public QuestionReadEntry(){

    }

    public QuestionReadEntry(BasicDBObject object){
        super(object);
    }
    //添加构造
    public QuestionReadEntry(
            ObjectId userId,
            ObjectId parentId,
            int type,
            int unReadNum
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("pid", parentId)
                .append("typ", type)
                .append("unr", unReadNum)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public QuestionReadEntry(
            ObjectId id,
            ObjectId userId,
            ObjectId parentId,
            int type,
            int unReadNum
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("pid", parentId)
                .append("typ", type)
                .append("unr", unReadNum)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
    public int getUnReadNum(){
        return getSimpleIntegerValue("unr");
    }

    public void setUnReadNum(int unReadNum){
        setSimpleValue("unr",unReadNum);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
