package com.pojo.jiaschool;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-29.
 * 学校开放功能表
 * schoolId             学校id               sid
 * type                 功能类型             typ    1 直播课堂开课权限
 * open                  是否开启            ope
 *
 */
public class SchoolFunctionEntry extends BaseDBObject{
    public SchoolFunctionEntry(){

    }

    public SchoolFunctionEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public SchoolFunctionEntry(
            ObjectId schoolId,
            int type,
            int open
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("tyo", type)
                .append("ope", open)
                .append("sid", schoolId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SchoolFunctionEntry(
            ObjectId id,
            int type,
            int open,
            ObjectId schoolId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("typ", type)
                .append("ope", open)
                .append("sid", schoolId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getType(){
        return getSimpleObjecIDValue("typ");
    }

    public void setType(ObjectId type){
        setSimpleValue("typ",type);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public int getOpen(){
        return getSimpleIntegerValue("ope");
    }

    public void setOpen(int open){
        setSimpleValue("ope",open);
    }
}
