package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-09-27.
 * 校管控内应用用户操作表
 * userId                       用户id                          uid
 * communityId                  社群id                          cid
 * appId                        操作应用                        aid
 * freeTime                     自由时间/防沉迷时间             ftm
 * controlType                  类型  0 放学后  1 上课中        cty
 * open                         0 关闭   1 开启                 ope
 * time                         时间戳                          tim
 */
public class ControlAppSchoolUserEntry extends BaseDBObject {
    public ControlAppSchoolUserEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ControlAppSchoolUserEntry(
                                     ObjectId userId,
                                     ObjectId appId,
                                     ObjectId communityId,
                                     int controlType,
                                     long freeTime,
                                     int open){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("uid", userId)
                .append("aid",appId)
                .append("cid", communityId)
                .append("cty", controlType)
                .append("ftm",freeTime)
                .append("ope",open)
                .append("ti", System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }


    public ObjectId getAppId(){
        return getSimpleObjecIDValue("aid");
    }

    public void setAppId(ObjectId appId){
        setSimpleValue("aid",appId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }


    public void setFreeTime(long freeTime){
        setSimpleValue("ftm",freeTime);
    }

    public long getFreeTime(){
        return getSimpleLongValue("ftm");
    }

    public void setControlType(int controlType){
        setSimpleValue("cty",controlType);
    }

    public int getControlType(){
        return getSimpleIntegerValue("cty");
    }

    public void setOpen(int open){
        setSimpleValue("ope",open);
    }

    public int getOpen(){
        return getSimpleIntegerValue("ope");
    }


    public void setTime(long time){
        setSimpleValue("ti",time);
    }

    public long getTime(){
        return getSimpleLongValue("ti");
    }

}
