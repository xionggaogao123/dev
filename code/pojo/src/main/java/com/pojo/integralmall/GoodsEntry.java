package com.pojo.integralmall;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.emarket.GoodsDTO;
import com.sys.constants.Constant;

/**
 * 
 * <简述>商品类
 * <详细描述>avatar 图片
 *        label标签
 *        name 名称
 *        cost 所需积分
 *        times 兑换次数
 *        description 简介
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class GoodsEntry extends BaseDBObject {

    public GoodsEntry() {
        
    }
    
    public GoodsEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject)dbObject);
    }
    
    public GoodsEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    
    public GoodsEntry(String avatar,
                      String label,
                      String name,
                      int cost,
                      long times,
                      String description) {
        BasicDBObject dbObject=new BasicDBObject()
            .append("avat", avatar)
            .append("lab", label)
            .append("name", name)
            .append("cost", Constant.ZERO)
            .append("times", times)
            .append("descrip", description)
            .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }
   
    
    
    public String getAvatar(){
        return getSimpleStringValue("avat");
    }

    public void setAvatar(String avatar){
        setSimpleValue("avat",avatar);
    }
    
    public String getLabel(){
        return getSimpleStringValue("lab");
    }

    public void setLabel(String label){
        setSimpleValue("lab",label);
    }
    
    public String getName(){
        return getSimpleStringValue("name");
    }

    public void setName(String name){
        setSimpleValue("name",name);
    }
    
    public void setCost(int cost){
        setSimpleValue("cost",cost);
    }

    public int getCost(){
        return getSimpleIntegerValue("cost");
    }
    
    public void setTimes(long times){
        setSimpleValue("times",times);
    }

    public long getTimes(){
        return getSimpleLongValue("times");
    }
    
    public String getDescription(){
        return getSimpleStringValue("descrip");
    }

    public void setDescription(String description){
        setSimpleValue("descrip",description);
    }
    
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
