package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/16.
 * id             id
 * type           类型                   1 常规   2 特殊   3 时间段   typ
 * week           星期                    （1-7）                     wek
 * dataTime       日期                     特殊日期                    dtm
 * startTime      开始时间                                             stm
 * endTime        结束时间                                              etm
 * communityId    社区id                                                cid
 * isr             0      通用             1      删除            2    学校
 */
public class ControlSchoolTimeEntry extends BaseDBObject {
    public ControlSchoolTimeEntry(){

    }
    public ControlSchoolTimeEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlSchoolTimeEntry(
            ObjectId parentId,
            ObjectId userId,
            int type,
            String dataTime,
            String startTime,
            String endTime,
            int week,
            ObjectId communityId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("uid", userId)
                .append("typ", type)
                .append("dtm", dataTime)
                .append("stm", startTime)
                .append("etm", endTime)
                .append("wek", week)
                .append("cid", communityId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlSchoolTimeEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            int type,
            String dataTime,
            String startTime,
            String endTime,
            int week,
            ObjectId communityId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("uid", userId)
                .append("typ", type)
                .append("dtm", dataTime)
                .append("stm", startTime)
                .append("etm", endTime)
                .append("wek", week)
                .append("cid", communityId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
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
    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
     public int getWeek(){
        return getSimpleIntegerValue("wek");
    }

    public void setWeek(int week){
        setSimpleValue("wek",week);
    }


    public void setManyClassTime(String manyClassTime){
        setSimpleValue("man",manyClassTime);
    }

    public String getManyClassTime(){
        return getSimpleStringValue("man");
    }


}
