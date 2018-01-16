package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/1/15.
 * 登陆时间记录表
 * id                                               id
 * userId           用户id                          uid
 * loginTime        登陆时间                        ltm
 * duration         持续时间                        dti
 * phoneType        此次登陆手机（ios/安卓）        pty
 */

public class LoginTimeEntry extends BaseDBObject {
     public LoginTimeEntry(){

     }

     public LoginTimeEntry(BasicDBObject object){
         super(object);
     }

    //添加构造
    public LoginTimeEntry(
            ObjectId userId,
            long loginTime,
            int duration,
            int phoneType
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("ltm", loginTime)
                .append("dti",duration)
                .append("pty",phoneType)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public LoginTimeEntry(
            ObjectId id,
            ObjectId userId,
            long loginTime,
            int duration,
            int phoneType
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("ltm", loginTime)
                .append("dti",duration)
                .append("pty", phoneType)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public long getLoginTime(){
        return getSimpleLongValue("ltm");
    }

    public void setLoginTime(String loginTime){
        setSimpleValue("ltm",loginTime);
    }

    public int getDuration(){
        return getSimpleIntegerValue("dti");
    }

    public void setDuration(int duration){
        setSimpleValue("dti",duration);
    }
    public int getPhoneType(){
        return getSimpleIntegerValue("pty");
    }

    public void setPhoneType(int phoneType){
        setSimpleValue("pty",phoneType);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
