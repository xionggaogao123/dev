package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-29.
 * 用户账户表(用来管理用户的支付账户信息)
 *
 * id
 * userId              uid                   用户id
 * accountName         ana                   账户名称
 * type                typ                   账户类型         1 支付宝      2 微信
 * rsaPrivateUserId    rpu                   账户id
 *
 */
public class UserAccountEntry extends BaseDBObject {

    public UserAccountEntry(){

    }
    public UserAccountEntry(BasicDBObject dbObject){
    setBaseEntry((BasicDBObject) dbObject);
    }

    public UserAccountEntry(ObjectId userId,
                            String accountName,
                            int type,
                            String rsaPrivateUserId){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("ana",accountName)
                .append("typ",type)
                .append("rpu", rsaPrivateUserId)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public String getRsaPrivateUserId(){
        return getSimpleStringValue("rpu");
    }

    public void setRsaPrivateUserId(String rsaPrivateUserId){
        setSimpleValue("rpu", rsaPrivateUserId);
    }

    public String getAccountName(){
        return getSimpleStringValue("ana");
    }

    public void setAccountName(String accountName){
        setSimpleValue("ana", accountName);
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

}
