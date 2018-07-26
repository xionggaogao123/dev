package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-07-26.
 * 用户协议表
 *
 * 用于记录用户是否同意协议
 * id                          id                    _id
 * userId                      用户id                uid
 * type                        类型                  typ  1 直播课堂开课协议       2 直播课堂上课协议
 * agree                       是否同意              agr
 * createTime                  创建时间              ctm
 * isRemove                    是否删除              isr
 *
 *
 *
 */
public class UserAgreementEntry extends BaseDBObject {

    public UserAgreementEntry(){

    }

    public UserAgreementEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public UserAgreementEntry(
            ObjectId userId,
            int type,
            int agree
    ){
        BasicDBObject dbObject  = new BasicDBObject()
                .append("uid",userId)
                .append("typ", type)
                .append("agr", agree)
                .append("ctm", new Date().getTime())
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getAgree(){
        return getSimpleIntegerValue("agr");
    }

    public void setAgree(int agree){
        setSimpleValue("agr",agree);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
