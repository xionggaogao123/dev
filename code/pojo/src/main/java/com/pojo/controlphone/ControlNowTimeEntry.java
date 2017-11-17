package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/17.
 * id
 * userId         uid
 * dataTime       日期                     特殊日期                    dtm
 * communityId     社区id                                              cid
 * startTime         stm
 * endTime           etm
 *
 */
public class ControlNowTimeEntry extends BaseDBObject {
    public ControlNowTimeEntry(){

    }
    public ControlNowTimeEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlNowTimeEntry(
            ObjectId userId,
            String dataTime,
            String startTime,
            String endTime,
            ObjectId communityId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("dtm", dataTime)
                .append("stm", startTime)
                .append("etm", endTime)
                .append("cid", communityId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlNowTimeEntry(
            ObjectId id,
            ObjectId userId,
            String dataTime,
            String startTime,
            String endTime,
            ObjectId communityId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("dtm", dataTime)
                .append("stm", startTime)
                .append("etm", endTime)
                .append("cid", communityId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public String getDataTime(){
        return getSimpleStringValue("dtm");
    }
    public void setDataTime(String dataTime){
        setSimpleValue("dtm",dataTime);
    }

    public String getStartTime(){
        return getSimpleStringValue("stm");
    }
    public void setStartTime(String startTime){
        setSimpleValue("stm",startTime);
    }

    public String getEndTime(){
        return getSimpleStringValue("etm");
    }
    public void setEndTime(String endTime){
        setSimpleValue("etm",endTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
