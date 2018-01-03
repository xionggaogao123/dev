package com.pojo.groupcollectmoney;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/29.
 * 群收款功能
 * {
 *   money:钱(计算到分)发起总额
 *   type:发起模式 1:人均模式 2:普通模式
 *   userId:发起人用户Id
 *   groupId:群组Id
 *   count:发起总人数
 *   description:群收款的说明
 *   status:0:支付进行中 1:支付已停止 2:已支付完成
 * }
 */
public class GroupCollectMoneyEntry extends BaseDBObject{

    public GroupCollectMoneyEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public GroupCollectMoneyEntry(long money,
                                  int type,
                                  ObjectId userId,
                                  ObjectId groupId,
                                  int count,
                                  String description){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("mo",money)
                .append("uid",userId)
                .append("gid",groupId)
                .append("ct",count)
                .append("ty",type)
                .append("st",Constant.ZERO)
                .append("des",description)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setStatus(int status){
        setSimpleValue("st",status);
    }

    public int getStatus(){
        return getSimpleIntegerValue("st");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setDescription(String description){
        setSimpleValue("des",description);
    }

    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setCount(int count){
        setSimpleValue("ct",count);
    }

    public int getCount(){
        return getSimpleIntegerValue("ct");
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setMoney(ObjectId money){
        setSimpleValue("mo",money);
    }

    public long getMoney(){
        return getSimpleLongValue("mo");
    }
}
