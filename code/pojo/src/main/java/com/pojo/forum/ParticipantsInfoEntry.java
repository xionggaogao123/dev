package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2017/1/11.
 * 参赛人员信息记录表
 * {
 *    ctr:creator创建者
 *    ptr:participator参赛者
 *    nm:name参赛者姓名
 *    age:age参赛者年龄
 *    sex:sex参赛者性别 0:女 1:男
 *    rl:relation联系方式
 *    sc:school就读学校
 * }
 */
public class ParticipantsInfoEntry extends BaseDBObject {

    public ParticipantsInfoEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }


    public ParticipantsInfoEntry(ObjectId participateId,ObjectId creator,ObjectId participator,String name,
                                 int age,int sex,String relation,String school){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,participateId)
                .append("ctr",creator)
                .append("ptr",participator)
                .append("nm",name)
                .append("age",age)
                .append("sex",sex)
                .append("rl",relation)
                .append("sc",school)
                .append("ir",0);
        setBaseEntry(dbObject);
    }


    public ObjectId getCreator(){
        return getSimpleObjecIDValue("ctr");
    }

    public ObjectId getParticipator(){
        return getSimpleObjecIDValue("ptr");
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm",name);
    }

    public int getAge(){
        return getSimpleIntegerValue("age");
    }

    public void setAge(int age){
        setSimpleValue("age",age);
    }

    public int getSex(){
        return getSimpleIntegerValue("sex");
    }

    public void setSex(int sex){
        setSimpleValue("sex",sex);
    }

    public String getRelation(){
        return getSimpleStringValue("rl");
    }

    public void setRelation(String relation){
        setSimpleValue("rl",relation);
    }

    public String getSchool(){
        return getSimpleStringValue("sc");
    }

    public void setSchool(String school){
        setSimpleValue("sc",school);
    }

}
