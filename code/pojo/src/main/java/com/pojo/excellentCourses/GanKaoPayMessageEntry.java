package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-09-19.
 * 与赶考网通信的支付记录通知对照表
 * String userId,String mobilePhone,String courseId,int price
 * userId              用户id                               uid
 * courseId            课程id                               cid
 * mobilePhone         电话                                 mbp
 * price               告知价格（分）                       pri
 * truePrice           实际价格（分）                       tpr
 * status              通知状态 0 未通知  1 已通知  2失效   sta
 * type                通知阶段                             typ  0  0秒  1. 10秒、2. 30秒、3. 5分钟、4. 30分钟
 * time                下次通知时间                         tim
 * endTime             结束通知时间                         etm
 * backTime            成功时间                             btm
 */
public class GanKaoPayMessageEntry extends BaseDBObject {

    public GanKaoPayMessageEntry(){

    }

    public GanKaoPayMessageEntry(BasicDBObject object){
        super(object);
    }


    //添加构造
    public GanKaoPayMessageEntry(
            ObjectId userId,
            ObjectId courseId,
            String mobilePhone,
            int price,
            int truePrice,
            int status,
            int type,
            long time,
            long endTime

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("cid", courseId)
                .append("mbp", mobilePhone)
                .append("pri", price)
                .append("tpr", truePrice)
                .append("sta", status)
                .append("typ",type)
                .append("tim", time)
                .append("etm",endTime)
                .append("btm",0)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }


    public ObjectId getCourseId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setCourseId(ObjectId courseId){
        setSimpleValue("cid",courseId);
    }
    public String getMobilePhone(){
        return getSimpleStringValue("mbp");
    }
    public void setMobilePhone(String mobilePhone){
        setSimpleValue("mbp", mobilePhone);
    }

    public long getTime(){
        return getSimpleLongValue("tim");
    }

    public void setTime(long time){
        setSimpleValue("tim",time);
    }
    public long getEndTime(){
        return getSimpleLongValue("etm");
    }

    public void setEndTime(long endTime){
        setSimpleValue("etm",endTime);
    }


    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }
    public long getBackTime(){
        return getSimpleLongValue("btm");
    }

    public void setBackTime(long backTime){
        setSimpleValue("btm",backTime);
    }
    public int getPrice(){
        return getSimpleIntegerValue("pri");
    }

    public void setPrice(int price){
        setSimpleValue("pri",price);
    }

    public int getTruePrice(){
        return getSimpleIntegerValue("tpr");
    }

    public void setTruePrice(double truePrice){
        setSimpleValue("tpr",truePrice);
    }


    public int getStatus(){
        return getSimpleIntegerValue("sta");
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
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
