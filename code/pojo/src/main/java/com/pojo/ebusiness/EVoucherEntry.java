package com.pojo.ebusiness;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**抵用券
 * Created by fl on 2016/3/7.
 * num:券号
 * uid：用户id
 * de：denomination 面额（分）
 * et:expiration time 过期时间 Long
 * st:state 状态 0未使用  1已使用  2已过期  3未发放  4已发放  5已删除
 * ac:activity 0:默认 1：活动优惠券
 */
public class EVoucherEntry extends BaseDBObject{

    public EVoucherEntry(){}

    public EVoucherEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public EVoucherEntry(ObjectId userId,String number, int denomination, long expTime, int state){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid", userId)
                .append("num", number)
                .append("de", denomination)
                .append("et", expTime)
                .append("st", state);
        setBaseEntry(baseEntry);
    }

    public EVoucherEntry(ObjectId userId,String number, int denomination, long expTime, int state,
                         int activity){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid", userId)
                .append("num", number)
                .append("de", denomination)
                .append("et", expTime)
                .append("st", state)
                .append("ac", activity);
        setBaseEntry(baseEntry);
    }


    public String getNumber(){
        return getSimpleStringValue("num");
    }

    public void setNumber(String number){
        setSimpleValue("num", number);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid", userId);
    }

    public int getDenomination(){
        return getSimpleIntegerValue("de");
    }

    public void setDenomination(int denomination){
        setSimpleValue("de", denomination);
    }

    public long getExpTime(){
        return getSimpleLongValue("et");
    }

    public void setExpTime(long expTime){
        setSimpleValue("et", expTime);
    }

    public int getState(){
        return getSimpleIntegerValue("st");
    }

    public void setState(int state){
        setSimpleValue("st", state);
    }


    public int getActivity(){
        if(getBaseEntry().containsField("ac")){
            return getSimpleIntegerValueDef("ac", 0);
        }else{
            return 0;
        }
    }

    public void setActivity(int activity){
        setSimpleValue("ac", activity);
    }



}
