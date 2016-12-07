package com.pojo.train;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/12/6.
 * {
 *     sid:instituteId 培训详情Id
 *     sc:score 评分
 *     uid:userId 用户Id
 *     cm:comment 评论内容
 *     ir:remove 删除标志
 * }
 */
public class CriticismEntry extends BaseDBObject{

    public CriticismEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public CriticismEntry(ObjectId instituteId,int score,ObjectId userId,
                          String comment){
        BasicDBObject dbObject=new BasicDBObject()
                .append("sid",instituteId)
                .append("sc",score)
                .append("uid",userId)
                .append("cm",comment)
                .append("ir",0);
        setBaseEntry(dbObject);
    }

    public ObjectId getInstituteId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setInstituteId(ObjectId instituteId){
        setSimpleValue("sid",instituteId);
    }

    public int getScore(){
        return getSimpleIntegerValue("sc");
    }

    public void setScore(int score){
        setSimpleValue("sc",score);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getComment(){
        return getSimpleStringValue("cm");
    }

    public void setComment(String comment){
        setSimpleValue("cm",comment);
    }

    public int getRemove(){
        return getSimpleIntegerValue("ir");
    }

    public void setRemove(int remove){
        setSimpleValue("ir",remove);
    }


}
