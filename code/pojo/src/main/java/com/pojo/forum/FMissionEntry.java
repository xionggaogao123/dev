package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/4.
 *
 * 任务列表
 *   {
 *       psid:personId 该用户id
 *       ti:time 任务完成时间
 *       sn:signIn 签到1：签到 0：未签到
 *       po：post 发帖/回帖 1: 发了 0：未发
 *       wf:welfare 福利 1：完成了 0：为完成
 *       ct:count 连续签到次数
 *   }
 */
public class FMissionEntry extends BaseDBObject {

    public FMissionEntry(){}

    public FMissionEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPersonId(ObjectId id){
        setSimpleValue("psid",id);
    }

    public String getTime(){
        return getSimpleStringValue("ti");
    }

    public void setTime(String time){
        setSimpleValue("ti", time);
    }

    public int getSignIn (){
        return getSimpleIntegerValue("sn");
    }

    public void setSignIn(int signIn) {
        setSimpleValue("sn", signIn);
    }

    public int getPost (){
        return getSimpleIntegerValue("po");
    }

    public void setPost(int post) {
        setSimpleValue("po", post);
    }

    public int getWelfare(){
        return getSimpleIntegerValue("wf");
    }

    public void setWelfare(int welfare) {
        setSimpleValue("wf", welfare);
    }

    public int getCount (){
        if(getBaseEntry().containsField("ct")) {
            return getSimpleIntegerValue("ct");
        }else{
            return -1;
        }
    }

    public void setCount(int count) {
        setSimpleValue("ct", count);
    }

}
