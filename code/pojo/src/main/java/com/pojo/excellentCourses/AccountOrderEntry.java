package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 *          mysql 充值记录
 * Created by James on 2018-06-05.
 * id
 * userId            充值订单用户                    uid
 * contactId         关联id                          cid
 * price             充值订单金额                    pri
 * account           充值账户
 * createTime        发起订单时间                    ctm
 * orderId           充值订单编号                    oid
 * type              充值订单方式                    typ            0 余额       1. 支付宝          2  微信
 * ip                充值ip                           ip
 * backTime          回调时间                         btm
 * tradeNo           第三方支付交易号                 tno
 * status            订单状态                         sta            0 未支付     1 已支付    2 已放弃    3 已失败
 *
 *
 */

public class AccountOrderEntry extends BaseDBObject {
    public AccountOrderEntry(){

    }

    public AccountOrderEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public AccountOrderEntry(
            ObjectId contactId,
            ObjectId userId,
            String account,
            String order,
            String back,
            double price,
            String orderId,
            int type,
            String ip,
            long backTime,
            String tradeN
    ){
        BasicDBObject dbObject = new BasicDBObject()
                .append("cid", contactId)
                .append("uid",userId)
                .append("aco",account)
                .append("ord", order)
                .append("bac",back)
                .append("pri", price)
                .append("oid",orderId)
                .append("typ",type)
                .append("ip",ip)
                .append("btm",backTime)
                .append("tra",tradeN)
                .append("ctm", new Date().getTime())
                .append("sta",Constant.ZERO)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public double getPrice(){
        return getSimpleDoubleValue("pri");
    }

    public void setPrice(double price){
        setSimpleValue("pri", price);
    }


    public String getOrderId(){
        return  getSimpleStringValue("oid");
    }
    public void setOrderId(String orderId){
        setSimpleValue("oid",orderId);
    }
    public String getAccount(){
        return  getSimpleStringValue("aco");
    }
    public void setAccount(String account){
        setSimpleValue("aco",account);
    }
    public String getOrder(){
        return  getSimpleStringValue("ord");
    }
    public void setOrder(String order){
        setSimpleValue("ord",order);
    }

    public String getBack(){
        return  getSimpleStringValue("bac");
    }
    public void setBack(String back){
        setSimpleValue("bac",back);
    }
    public String getIp(){
        return  getSimpleStringValue("ip");
    }
    public void setIp(String ip){
        setSimpleValue("ip",ip);
    }
    public String getTradeN(){
        return  getSimpleStringValue("tra");
    }
    public void setTradeN(String tradeN){
        setSimpleValue("tra",tradeN);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
    public int getStatus(){
        return getSimpleIntegerValue("sta");
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(int createTime){
        setSimpleValue("ctm",createTime);
    }
    public long getBackTime(){
        return getSimpleLongValue("btm");
    }

    public void setBackTime(long backTime){
        setSimpleValue("btm",backTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
