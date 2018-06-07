package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * mysql 账户
 * Created by James on 2018-06-06.
 * id
 * userId             用户id                         uid
 * account            金额                           acc
 * suoDing            是否锁定                       suo
 * type               账户状态                       typ               0 正常    1  异常
 *
 *
 */
public class AccountFrashEntry extends BaseDBObject {

    public AccountFrashEntry(){

    }

    public AccountFrashEntry(BasicDBObject object){
        super(object);
    }


    //添加构造
    public AccountFrashEntry(
            ObjectId userId,
            double account,
            int suoDing,
            int type

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("acc", account)
                .append("suo", suoDing)
                .append("typ", type)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getSuoDing(){
        return getSimpleIntegerValue("suo");
    }
    public void setSuoDing(int suoDing){
        setSimpleValue("suo",suoDing);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ", type);
    }

    public double getAccount(){
        return getSimpleDoubleValue("acc");
    }

    public void setAccount(double account){
        setSimpleValue("acc",account);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
