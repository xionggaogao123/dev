package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 使用应用记录表
 * Created by James on 2018/1/15.
 * id
 * userId           用户id              uid
 * moduleType       使用模块            mty
 * moduleName       模块名              mnm
 * number           使用次数            num
 *
 */
public class ModuleNumberEntry extends BaseDBObject {
    public ModuleNumberEntry(){

    }

    public ModuleNumberEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ModuleNumberEntry(
            ObjectId userId,
            int moduleType,
            String moduleName,
            int number
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("mty", moduleType)
                .append("mnm",moduleName)
                .append("num",number)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ModuleNumberEntry(
            ObjectId id,
            ObjectId userId,
            int moduleType,
            String moduleName,
            int number
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("mty", moduleType)
                .append("mnm",moduleName)
                .append("num",number)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getModuleType(){
        return getSimpleIntegerValue("mty");
    }

    public void setModuleType(int moduleType){
        setSimpleValue("mty",moduleType);
    }

    public String getModuleName(){
        return getSimpleStringValue("mnm");
    }
    public void setModuleName(String moduleName){
        setSimpleValue("mnm",moduleName);
    }

    public int getNumber(){
        return getSimpleIntegerValue("num");
    }

    public void setNumber(int number){
        setSimpleValue("num",number);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
