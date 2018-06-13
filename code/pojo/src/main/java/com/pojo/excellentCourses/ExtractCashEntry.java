package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-06-06.
 * 提现申请表
 * id
 * userId                申请人                               uid
 * jiaId                 家校美id                             jid
 * cash                  申请金额                             cas
 * account               提现账户                             acc
 * accountType           账户类型                             aty  1支付宝  2 微信
 * createTime            申请时间                             rtm
 * type                  状态                                 typ   （1. 申请中 2 通过 3 拒绝  4 账户有误 5 金额有误  6 其他）
 * dateTime              处理时间                             dtm
 */
public class ExtractCashEntry extends BaseDBObject {



    public ExtractCashEntry(){

    }
    public ExtractCashEntry(BasicDBObject dbObject){
        setBaseEntry(dbObject);
    }

    public ExtractCashEntry(ObjectId userId,
                            String jiaId,
                            double cash,
                            String account,
                            int accountType,
                            int type){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("jid", jiaId)
                .append("acc",account)
                .append("aty",accountType)
                .append("typ",type)
                .append("cas", cash)
                .append("ctm",new Date().getTime())
                .append("dtm",0l)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }



    public double getCash(){
        return getSimpleDoubleValue("cas");
    }

    public void setCash(double cash){
        setSimpleValue("cas",cash);
    }

    public String getAccount(){
        return getSimpleStringValue("acc");
    }

    public void setAccount(String account){
        setSimpleValue("acc", account);
    }

    public String getJiaId(){
        return getSimpleStringValue("jid");
    }

    public void setJiaId(String jiaId){
        setSimpleValue("jid", jiaId);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }

    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }
    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ", type);
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
    public int getAccountType(){
        return getSimpleIntegerValueDef("aty",1);
    }

    public void setAccountType(int accountType){
        setSimpleValue("aty",accountType);
    }

}
