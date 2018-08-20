package com.pojo.lancustom;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 16:07
 * @Description:
 */
public class MonetaryOrdersEntry extends BaseDBObject {


    public MonetaryOrdersEntry() {

    }

    public MonetaryOrdersEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }

    public MonetaryOrdersEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }


    public MonetaryOrdersEntry(ObjectId userId,
                               ObjectId goodId,
                               ObjectId addressId,
                               int goodNum,
                               String style,
                               Double money,
                               String orderNo,
                               String excompanyNo,//物快递公司编号
                               String expressNo,//快递编号
                               String orderTimeStr,//生成订单时间
                               String status,//订单状态 0 未支付 1 支付成功
                               String payOrderTimeStr,// 支付订单时间
                               String payMethod,//支付方式 0 微信支付 1 支付宝支付
                               String tradeNo,//生成的提供支付厂商的单号
                               String isState,//是否已经申述 0否 1是
                               String stateReason,//申述内容
                               String isAcceptance,//状态 0未受理 1已受理
                               String acceptanceStr//受理描述
    ) {
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("goodId", goodId)
                .append("addressId", addressId)
                .append("goodNum", goodNum)
                .append("style", style)
                .append("money", money)
                .append("orderNo", orderNo)
                .append("excompanyNo", excompanyNo)
                .append("expressNo", expressNo)
                .append("orderTimeStr", orderTimeStr)
                .append("isr", Constant.ZERO)
                .append("status", status)
                .append("payOrderTimeStr", payOrderTimeStr)
                .append("payMethod", payMethod)
                .append("tradeNo", tradeNo)
                .append("isState", isState)
                .append("stateReason", stateReason)
                .append("isAcceptance", isAcceptance)
                .append("acceptanceStr", acceptanceStr)
                ;
        setBaseEntry(dbObject);
    }

    public ObjectId getUid(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUid(ObjectId uid){
        setSimpleValue("uid",uid);
    }

    public ObjectId getAid(){
        return getSimpleObjecIDValue("addressId");
    }

    public void setAid(ObjectId addressId){
        setSimpleValue("addressId",addressId);
    }

    public ObjectId getGid(){
        return getSimpleObjecIDValue("goodId");
    }

    public void setGid(ObjectId goodId){
        setSimpleValue("goodId",goodId);
    }

    public void setGoodNum(int goodNum){
        setSimpleValue("goodNum",goodNum);
    }

    public int getGoodNum(){
        return getSimpleIntegerValue("goodNum");
    }

    public void setStatus(String status){
        setSimpleValue("status",status);
    }

    public String getStatus(){
        return getSimpleStringValue("status");
    }


    public String getExcompanyNo(){
        return getSimpleStringValue("excompanyNo");
    }

    public void setExcompanyNo(String excompanyNo){
        setSimpleValue("excompanyNo",excompanyNo);
    }

    public String getExpressNo(){
        return getSimpleStringValue("expressNo");
    }

    public void setExpressNo(String expressNo){
        setSimpleValue("expressNo",expressNo);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public String getOrderTimeStr(){
        return getSimpleStringValue("orderTimeStr");
    }

    public void setOrderTimeStr(String orderTimeStr){
        setSimpleValue("orderTimeStr",orderTimeStr);
    }

    public String getPayOrderTimeStr(){
        return getSimpleStringValue("payOrderTimeStr");
    }

    public void setPayOrderTimeStr(String payOrderTimeStr){
        setSimpleValue("payOrderTimeStr",payOrderTimeStr);
    }

    public String getPayMethod(){
        return getSimpleStringValue("payMethod");
    }

    public void setPayMethod(String payMethod){
        setSimpleValue("payMethod",payMethod);
    }

    public Double getMoney(){
//        return Double.parseDouble(getSimpleStringValue("money"));
        //数据库存的是两位 mongo find出来很多位小数 在此解决
        return new BigDecimal(getSimpleStringValue("money")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setMoney(Double money){
        setSimpleValue("money",money);
    }

    public String getStyle(){
        return getSimpleStringValue("style");
    }

    public void setStyle(String style){
        setSimpleValue("style",style);
    }
    public String getOrderNo(){
        return getSimpleStringValue("orderNo");
    }

    public void setOrderNo(String orderNo){
        setSimpleValue("orderNo",orderNo);
    }

    public String getTradeNo(){
        return getSimpleStringValue("tradeNo");
    }

    public void setTradeNo(String tradeNo){
        setSimpleValue("tradeNo",tradeNo);
    }



    public String getIsState(){
        return getSimpleStringValue("isState");
    }

    public void setIsState(String isState){
        setSimpleValue("money",isState);
    }

    public String getStateReason(){
        return getSimpleStringValue("stateReason");
    }

    public void setStateReason(String stateReason){
        setSimpleValue("stateReason",stateReason);
    }

    public String getIsAcceptance(){
        return getSimpleStringValue("isAcceptance");
    }

    public void setIsAcceptance(String isAcceptance){
        setSimpleValue("isAcceptance",isAcceptance);
    }

    public String getAcceptanceStr(){
        return getSimpleStringValue("acceptanceStr");
    }

    public void setAcceptanceStr(String acceptanceStr){
        setSimpleValue("acceptanceStr",acceptanceStr);
    }


}
