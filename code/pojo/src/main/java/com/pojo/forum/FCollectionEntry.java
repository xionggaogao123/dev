package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 *
 * 收藏帖子/版块
 * {
 *     uid : userId 发帖人id
 *     psid : postSectionId 帖子/版块id
 *     tp : type 类型 0：帖子 1：版块
 *     ti : time 收藏时间
 * }
 *
 */
public class FCollectionEntry extends BaseDBObject{

    public FCollectionEntry(){
        super();
    }

    public FCollectionEntry(ObjectId userId,ObjectId postSectionId,int type,long time){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid",userId)
                .append("psid",postSectionId)
                .append("tp",type)
                .append("ti",time);
        setBaseEntry(baseEntry);
    }

    public FCollectionEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId id){
        setSimpleValue("uid",id);
    }

    public ObjectId getPostSectionId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPostSectionId(ObjectId id){
        setSimpleValue("psid",id);
    }

    public int getType(){
        return getSimpleIntegerValue("tp");
    }

    public void setType(int type){
        setSimpleValue("tp",type);
    }

    public Long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(Long time){
        setSimpleValue("ti", time);
    }

}
