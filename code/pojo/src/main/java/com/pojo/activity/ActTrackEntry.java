package com.pojo.activity;

import com.mongodb.BasicDBObject;
import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActTrackType;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by Hao on 2015/3/4.
 */
public class ActTrackEntry extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4672697416884528082L;
	/*
    *
    *
    * rid    关联id  肯能是活动Id也可能是userId(视动态类型而定)
    * att    动态类型
    * uid    用户id
    * ct     创建时间
    * atd    动态来源于什么设备
    *
    *
    * */
    public ActTrackEntry (){
        BasicDBObject basicDBObject=new BasicDBObject("rid",null).append("uid",null).append("ct",null).append("atd",null);
        setBaseEntry(basicDBObject);
    }
    public ActTrackEntry(BasicDBObject basicDBObject){
        super(basicDBObject);
    }

    public ObjectId getRelatedId(){
        return getSimpleObjecIDValue("rid");
    }
    public int getActTrackType(){
        return getSimpleIntegerValue("att");
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public long getCreateTime(){
        return getSimpleLongValue("ct");
    }
    public int getActTrackDevice(){
        return getSimpleIntegerValue("atd");
    }


    //set
    public void setRelatedId(ObjectId objectId){
         setSimpleValue("rid",objectId);
    }
    public void setActTrackType(ActTrackType actTrackType){
        setSimpleValue("att",actTrackType.getState());
    }
    public void setUserId(ObjectId objectId){
        setSimpleValue("uid",objectId);
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ct",createTime);
    }
    public void setActTrackDevice(ActTrackDevice actTrackDevice){
        setSimpleValue("atd",actTrackDevice.getState());
    }
}
