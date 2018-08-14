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
 * orderId           直接购买的标志      oid
 * userId            购买人              uid
 * isBuy             是否购买            isb  // 0 未购买      1已购买
 * price             购买价格            pri
 * function          付款方式            fun   //0 美豆余额   1 支付宝   2 微信   3 后台添加   4 系统添加课程附带添加
 * type              状态                typ      0 使用中    1 未使用   2 已过期  3 退款中   4 已取消  5  后台删除
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
            String orderId,
            int isBuy,
            double price,
            int function,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("pid", parentId)
                .append("cid", contactId)
                .append("oid",orderId)
                .append("isb", isBuy)
                .append("pri", price)
                .append("cnp", function)
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
            String orderId,
            int isBuy,
            double price,
            int function,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("pid", parentId)
                .append("cid", contactId)
                .append("oid",orderId)
                .append("isb", isBuy)
                .append("pri", price)
                .append("cnp", function)
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


    public String getOrderId(){
        return  getSimpleStringValue("oid");
    }
    public void setOrderId(String orderId){
        setSimpleValue("oid",orderId);
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


    public double getPrice(){
        return getSimpleDoubleValue("pri");
    }

    public void setPrice(double price){
        setSimpleValue("pri",price);
    }

    public int getFunction(){
        return getSimpleIntegerValue("cnp");
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
