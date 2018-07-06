package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-07-02.
 * 聊天禁言表
 * id
 * userId                     用户id                         uid
 * memberId                   处理人                         mid
 * groupId                    群组id                         gid
 * communityId                社群id                         cid
 * type                       处理手段                       typ         0 禁言   1 禁用
 * startTime                  开始时间                       stm
 * endTime                    结束时间                       etm
 */
public class CommunitySpeakingEntry extends BaseDBObject {
    public CommunitySpeakingEntry(){

    }

    public CommunitySpeakingEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public CommunitySpeakingEntry(
            ObjectId userId,
            ObjectId memberId,
            ObjectId groupId,
            ObjectId communityId,
            int type,
            long startTime,
            long endTime
    ){
        BasicDBObject dbObject  = new BasicDBObject()
                .append("uid",userId)
                .append("mid", memberId)
                .append("gid",groupId)
                .append("cid",communityId)
                .append("typ",type)
                .append("stm",startTime)
                .append("etm",endTime)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getMemberId(){
        return getSimpleObjecIDValue("mid");
    }
    public void setMemberId(ObjectId memberId){
        setSimpleValue("mid",memberId);
    }
    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }
    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }
    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public long getStartTime(){
        return getSimpleLongValue("stm");
    }

    public void setStartTime(long startTime){
        setSimpleValue("stm",startTime);
    }
    public long getEndTime(){
        return getSimpleLongValue("etm");
    }

    public void setEndTime(long endTime){
        setSimpleValue("etm",endTime);
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
