package com.pojo.excellentCourses;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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
 * source               来源                        sou/0 美豆余额   1 支付宝   2 微信   3 后台添加   4 系统添加课程附带添加
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
            List<ObjectId> classList,
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
                .append("clt",classList)
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
    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }
    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public String getSchoolName(){
        return getSimpleStringValue("snm");
    }
    public void setSchoolName(String schoolName){
        setSimpleValue("snm",schoolName);
    }

    public ObjectId getCoursesId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setCoursesId(ObjectId coursesId){
        setSimpleValue("cid",coursesId);
    }

    public String getCoursesName(){
        return getSimpleStringValue("cnm");
    }

    public void setCoursesName(long coursesName){
        setSimpleValue("cnm",coursesName);
    }

    public String getOrder(){
        return getSimpleStringValue("ord");
    }

    public void setOrder(long order){
        setSimpleValue("ord",order);
    }

    public double getPrice(){
        return getSimpleDoubleValue("pri");
    }

    public void setPrice(double price){
        setSimpleValue("pri",price);
    }

    public int getSource(){
        return getSimpleIntegerValue("sou");
    }

    public void setSource(int source){
        setSimpleValue("sou",source);
    }

    public void setClassList(List<ObjectId> classList){
        setSimpleValue("clt", MongoUtils.convert(classList));
    }

    public List<ObjectId> getClassList(){
        ArrayList<ObjectId> classList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                classList.add((ObjectId)obj);
            }
        }
        return classList;
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("tyo",type);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
