package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-04-26.
 * 课时订单表
 * id                id                   id
 * contactId         关联课程            cid
 * parentId          关联课时            pid
 * userId            购买人              uid
 * isBuy             是否购买            isb
 * price             购买价格            pri
 * function          付款方式            fun
 * type              状态                typ      0 未使用    1 使用中   2 已过期
 *
 */
public class ClassOrderEntry extends BaseDBObject {




    public ClassOrderEntry(){

    }

    public ClassOrderEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ClassOrderEntry(
            ObjectId userId,
            ObjectId parentId,
            ObjectId contactId,
            int isBuy,
            int price,
            int function,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("pid", parentId)
                .append("cid", contactId)
                .append("isb", isBuy)
                .append("pri", price)
                .append("fun", function)
                .append("typ", type)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ClassOrderEntry(
            ObjectId id,
            ObjectId userId,
            ObjectId parentId,
            ObjectId contactId,
            int isBuy,
            int price,
            int function,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("pid", parentId)
                .append("cid", contactId)
                .append("isb", isBuy)
                .append("pri", price)
                .append("fun", function)
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
    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }
    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }


    public int getIsBuy(){
        return getSimpleIntegerValue("isb");
    }

    public void setIsBuy(int isBuy){
        setSimpleValue("isb",isBuy);
    }


    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }


    public int getPrice(){
        return getSimpleIntegerValue("pri");
    }

    public void setPrice(int price){
        setSimpleValue("pri",price);
    }

    public int getFunction(){
        return getSimpleIntegerValue("fun");
    }

    public void setFunction(int function){
        setSimpleValue("cnp",function);
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
