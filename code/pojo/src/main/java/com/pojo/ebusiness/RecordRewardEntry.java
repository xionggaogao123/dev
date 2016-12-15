package com.pojo.ebusiness;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/12/15.
 * {
 *     uid:userId用户Id
 *     add:address 用户填写地址
 *     tel:telephone 电话
 *     dec:description 用户中奖描述
 *     ty:type 0:抽奖 1:记录是否转发
 * }
 */
public class RecordRewardEntry extends BaseDBObject{
    public RecordRewardEntry(){

    }

    public RecordRewardEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }


    public RecordRewardEntry(ObjectId userId, String address, String telephone, String description){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid",userId)
                .append("add",address)
                .append("tel",telephone)
                .append("desc",description)
                .append("ty",0)
                .append("ir",0);
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getAddress(){
        return getSimpleStringValue("add");
    }

    public void setAddress(String address){
        setSimpleValue("add",address);
    }

    public String getTelephone(){
        return getSimpleStringValue("tel");
    }

    public void setTelephone(String telephone){
        setSimpleValue("tel",telephone);
    }

    public String getDescription(){
        return getSimpleStringValue("desc");
    }

    public void setDescription(String description){
        setSimpleValue("desc",description);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getRemove(){
        return getSimpleIntegerValue("ir");
    }

    public void setRemove(int remove){
        setSimpleValue("ir",remove);
    }

}
