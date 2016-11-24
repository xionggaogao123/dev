package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 签到  每个用户一条，time为最后一次签到时间
 * {
 *     uid: userId 用户id
 *     dt: date 签到时间（年月日：2015-05-30）
 *     ct: count 连续签到次数
 * }
 */
public class FSignEntry extends BaseDBObject{
    public FSignEntry(){
        super();
    }

    public FSignEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public FSignEntry(ObjectId userId){
        BasicDBObject baseEntry = new BasicDBObject("uid",userId);
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId id){
        setSimpleValue("uid",id);
    }

    public long getDate(){
        return getSimpleLongValue("dt");
    }

    public void setDate(long date){
        setSimpleValue("dt",date);
    }

    public int getCount(){
        return getSimpleIntegerValue("ct");
    }

    public void setCount(int count){
        setSimpleValue("ct",count);
    }

}
