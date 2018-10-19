package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by James on 2018-10-19.
 *
 * id                                               id
 * orderTime            下单日期                    otm
 * userId               用户id                      uid
 * schoolId             学校id                      sid
 * schoolName           学校名称                    snm
 * coursesId            课程id                      cid
 * coursesName          课程名称                    cnm
 * type                 1 收入   2 支出             typ
 * List<ObjectId>       购买课节id                  clt
 * price                购买金额                    pri
 * order                订单号                      ord
 * source               来源                        sou
 */
public class CoursesOrderResultEntry extends BaseDBObject {
    public CoursesOrderResultEntry(){

    }

    public CoursesOrderResultEntry(BasicDBObject dbObject){
        super(dbObject);
    }


    //添加构造
    public CoursesOrderResultEntry(
            long orderTime,
            ObjectId userId,
            ObjectId schoolId,
            String schoolName,
            ObjectId coursesId,
            String coursesName,
            int type,
            List<ObjectId> classIdList,
            double price,
            String order,
            int source
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("otm", orderTime)
                .append("uid", userId)
                .append("cid", schoolId)
                .append("snm", schoolName)
                .append("cid", coursesId)
                .append("cnm", coursesName)
                .append("typ",type)
                .append("clt",classIdList)
                .append("pri",price)
                .append("ord",order)
                .append("sou",source)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }
    public long getOrderTime(){
        return getSimpleLongValue("otm");
    }
    public void setOrderTime(String orderTime){
        setSimpleValue("otm",orderTime);
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

    public String getUserName(){
        return getSimpleStringValue("unm");
    }
    public void setUserName(String userName){
        setSimpleValue("unm",userName);
    }

    public String getUserIp(){
        return getSimpleStringValue("uip");
    }
    public void setUserIp(String userIp){
        setSimpleValue("uip",userIp);
    }

    public String getEnterTime(){
        return getSimpleStringValue("etm");
    }

    public void setEnterTime(long enterTime){
        setSimpleValue("etm",enterTime);
    }


    public String getLeaveTime(){
        return getSimpleStringValue("ltm");
    }

    public void setLeaveTime(long leaveTime){
        setSimpleValue("ltm",leaveTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
