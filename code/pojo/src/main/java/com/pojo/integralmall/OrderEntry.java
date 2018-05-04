package com.pojo.integralmall;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 
 * <简述>订单表
 * <详细描述>goodId  商品id
 *        goodNum 商品数量
 *        excompanyNo 快递公司编号
 *        expressNo 快递编号
 *        isState 是否申述 0未申述 1已申述
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class OrderEntry extends BaseDBObject {

    public OrderEntry() {
        
    }
    
    public OrderEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }
    
    public OrderEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    
    public OrderEntry(ObjectId userId,
                      ObjectId goodId,
                      ObjectId addressId,
                      int goodNum,
                      String excompanyNo,
                      String expressNo,
                      int isState,
                      long orderTime,
                      String orderTimeStr,
                      String stateReason
                      ) {
        BasicDBObject dbObject=new BasicDBObject()
            .append("uid", userId)
            .append("gid", goodId)
            .append("aid", addressId)
            .append("gNum", goodNum)
            .append("ecNo", excompanyNo)
            .append("epNo", expressNo)
            .append("ist", isState)
            .append("sRea", stateReason)
            .append("ot", orderTime)
            .append("ots", orderTimeStr)
            .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }
    
    public ObjectId getUid(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUid(ObjectId uid){
        setSimpleValue("uid",uid);
    }
    
    public ObjectId getAid(){
        return getSimpleObjecIDValue("aid");
    }

    public void setAid(ObjectId aid){
        setSimpleValue("aid",aid);
    }
    
    public ObjectId getGid(){
        return getSimpleObjecIDValue("gid");
    }

    public void setGid(ObjectId gid){
        setSimpleValue("gid",gid);
    }
    
    public void setGoodNum(int goodNum){
        setSimpleValue("gNum",goodNum);
    }

    public int getGoodNum(){
        return getSimpleIntegerValue("gNum");
    }
    
    public void setIsState(int isState){
        setSimpleValue("ist",isState);
    }

    public int getIsState(){
        return getSimpleIntegerValueDef("ist",Constant.ZERO);
    }
    
    public String getExcompanyNo(){
        return getSimpleStringValue("ecNo");
    }

    public void setExcompanyNo(String excompanyNo){
        setSimpleValue("ecNo",excompanyNo);
    }
    
    public String getExpressNo(){
        return getSimpleStringValue("epNo");
    }

    public void setExpressNo(String expressNo){
        setSimpleValue("epNo",expressNo);
    }
    
    public String getStateReason(){
        return getSimpleStringValue("sRea");
    }

    public void setStateReason(String stateReason){
        setSimpleValue("sRea",stateReason);
    }
    
    
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
    
    public String getOrderTimeStr(){
        return getSimpleStringValue("ots");
    }

    public void setOrderTimeStr(String orderTimeStr){
        setSimpleValue("ots",orderTimeStr);
    }
}
