package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-09-12.
 *
 *  直播课堂业务扩展表
 *
 * id                    记录id                      id
 * contactId             关联课程id                  cid
 * classNumber           课程唯一ID                  cnm
 * sellName              销售者姓名                  snm
 * sellId                销售者id                    sid
 * province              省份                        pro
 * city                  城市                        cit
 * assistantName         助教姓名                    anm
 * assistantId           助教id                      aid
 *
 *
 */
public class CoursesBusinessEntry extends BaseDBObject {
    public CoursesBusinessEntry(){

    }

    public CoursesBusinessEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public CoursesBusinessEntry(
            ObjectId contactId,
            String classNumber,
            String sellName,
            ObjectId sellId,
            String province,
            String city,
            String assistantName,
            ObjectId assistantId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("cid", contactId)
                .append("cnm",classNumber)
                .append("snm", sellName)
                .append("sid", sellId)
                .append("pro", province)
                .append("cit", city)
                .append("anm", assistantName)
                .append("aid", assistantId)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }
    public String getSellName(){
        return getSimpleStringValue("snm");
    }
    public void setSellName(String sellName){
        setSimpleValue("snm",sellName);
    }

    public String getAssistantName(){
        return getSimpleStringValue("anm");
    }

    public void setAssistantName(String assistantName){
        setSimpleValue("anm",assistantName);
    }
    public String getClassNumber(){
        return getSimpleStringValue("cnm");
    }

    public void setClassNumber(String classNumber){
        setSimpleValue("cnm",classNumber);
    }

    public ObjectId getSellId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSellId(ObjectId sellId){
        setSimpleValue("sid",sellId);
    }

    public ObjectId getAssistantId(){
        return getSimpleObjecIDValue("aid");
    }

    public void setAssistantId(ObjectId assistantId){
        setSimpleValue("aid",assistantId);
    }

    public String getProvince(){
        return getSimpleStringValue("pro");
    }
    public void setProvince(String province){
        setSimpleValue("pro", province);
    }
    public String getCity(){
        return getSimpleStringValue("cit");
    }
    public void setCity(String city){
        setSimpleValue("cit", city);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}
