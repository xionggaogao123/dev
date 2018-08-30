package com.pojo.excellentCourses;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-08-28.
 * 订单管理表（退课订单和删除订单管理）
 *
 * userId            用户id                         uid
 * role              用户身份                       rol    
 * deleteId          操作人id                       did
 * contactId         关联课程id                     cid
 * List<ObjectId>    classIdList关联课节id          clt
 * List<ObjectId>    orderIdList关联订单id          olt
 * price             订单总价                       pri
 * orderId           订单号                         oid
 * orderType         订单类型                       oty      //0 美豆余额   1 支付宝   2 微信   3 后台添加   4 系统添加课程附带添加
 * createTime        操作时间                       ctm
 * status            状态                           sta       0     正常             1     删除            2     退课
 *
 *
 *
 *
 */
public class BackOrderEntry extends BaseDBObject {
    public BackOrderEntry(){

    }

    public BackOrderEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public BackOrderEntry(
            ObjectId contactId,
            ObjectId userId,
            ObjectId deleteId,
            List<ObjectId> classIdList,
            List<ObjectId> orderIdList,
            double price,
            String orderId,
            int orderType,
            int status,
            int role,
            int money
    ){
        BasicDBObject dbObject = new BasicDBObject()
                .append("cid", contactId)
                .append("uid",userId)
                .append("did",deleteId)
                .append("clt", classIdList)
                .append("olt",orderIdList)
                .append("pri", price)
                .append("oid",orderId)
                .append("oty",orderType)
                .append("sta",status)
                .append("rol",role)
                .append("mon",money)
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

    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public ObjectId getDeleteId(){
        return getSimpleObjecIDValue("did");
    }

    public void setDeleteId(ObjectId deleteId){
        setSimpleValue("did",deleteId);
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

    public int getOrderType(){
        return getSimpleIntegerValue("oty");
    }

    public void setRole(int role){
        setSimpleValue("rol",role);
    }
    public int getRole(){
        return getSimpleIntegerValue("rol");
    }

    public void setMoney(int money){
        setSimpleValue("mon",money);
    }
    public int getMoney(){
        return getSimpleIntegerValue("mon");
    }

    public void setOrderType(int orderType){
        setSimpleValue("oty",orderType);
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

    public void setClassIdList(List<ObjectId> classIdList){
        setSimpleValue("clt", MongoUtils.convert(classIdList));
    }

    public List<ObjectId> getClassIdList(){
        ArrayList<ObjectId> classIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                classIdList.add((ObjectId)obj);
            }
        }
        return classIdList;
    }
    public void setOrderIdList(List<ObjectId> orderIdList){
        setSimpleValue("olt", MongoUtils.convert(orderIdList));
    }

    public List<ObjectId> getOrderIdList(){
        ArrayList<ObjectId> orderIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("olt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                orderIdList.add((ObjectId)obj);
            }
        }
        return orderIdList;
    }



    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}
