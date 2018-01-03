package com.pojo.groupcollectmoney;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/29.
 * 订单信息生成
 * {
 *     发起的群收款Id:groupCollectId
 *     群成员Id:userId
 *     群组Id:groupId
 *     支付费用/操作金额:amount
 *     商户的授权资金订单号:outOrderNo
 *     商户本次资金操作的请求流水号:outRequestNo
 *     订单标题:orderTitle
 *     支付超时时间:payTimeout
 *     支付宝的资金授权订单号:authNo
 *     支付宝的资金操作流水号:operationId
 *     资金授权成功时间:gmtTrans
 *     支付状态:0:未支付 1:支付已停止 2:已支付完成
 * }
 */
public class GroupUserExpenseEntry extends BaseDBObject{

    public GroupUserExpenseEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public GroupUserExpenseEntry(ObjectId groupCollectId,
                                 ObjectId userId,
                                 ObjectId groupId,
                                 long amount,
                                 ObjectId outOrderNo,
                                 ObjectId outRequestNo,
                                 String orderTitle
                                 ){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("gci",groupCollectId)
                .append("uid",userId)
                .append("gid",groupId)
                .append("am",amount)
                .append("ono",outOrderNo)
                .append("oro",outRequestNo)
                .append("or",orderTitle)
                .append("pt", Constant.EMPTY)
                .append("ano",Constant.EMPTY)
                .append("oid",Constant.EMPTY)
                .append("gti",Constant.EMPTY)
                .append("st",Constant.ZERO)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setGroupCollectId(ObjectId groupCollectId){
        setSimpleValue("gci",groupCollectId);
    }

    public ObjectId getGroupCollectId(){
        return getSimpleObjecIDValue("gci");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setAmount(long amount){
        setSimpleValue("am",amount);
    }

    public long getAmount(){
        return getSimpleLongValue("am");
    }

    public void setOutOrderNo(ObjectId outOrderNo){
        setSimpleValue("ono",outOrderNo);
    }

    public ObjectId getOutOrderNo(){
        return getSimpleObjecIDValue("ono");
    }

    public void setOutRequestNo(ObjectId outRequestNo){
        setSimpleValue("oro",outRequestNo);
    }

    public ObjectId getOutRequestNo(){
        return getSimpleObjecIDValue("oro");
    }

    public void setOrderTitle(String orderTitle){
        setSimpleValue("or",orderTitle);
    }

    public String getOrderTitle(){
        return getSimpleStringValue("or");
    }

    public void setPayTimeout(String payTimeout){
        setSimpleValue("pt",payTimeout);
    }

    public String getPayTimeout(){
        return getSimpleStringValue("pt");
    }

    public void setAuthNo(String authNo){
        setSimpleValue("ano",authNo);
    }

    public String getAuthNo(){
        return getSimpleStringValue("ano");
    }

    public void setOperationId(String operationId){
        setSimpleValue("oid",operationId);
    }

    public String getOperationId(){
        return getSimpleStringValue("oid");
    }

    public void setGmtTrans(String gmtTrans){
        setSimpleValue("gti",gmtTrans);
    }

    public String getGmtTrans(){
        return getSimpleStringValue("gti");
    }

    public void setStatus(int status){
        setSimpleValue("st",status);
    }

    public int getStatus(){
        return getSimpleIntegerValue("st");
    }
}
