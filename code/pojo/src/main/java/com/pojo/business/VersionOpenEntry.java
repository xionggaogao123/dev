package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-01.
 */
public class VersionOpenEntry extends BaseDBObject {
    public VersionOpenEntry(){

    }

    public VersionOpenEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public VersionOpenEntry(
            int moduleType,
            String moduleName
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("mty", moduleType)
                .append("mnm",moduleName)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public VersionOpenEntry(
            ObjectId id,
            int moduleType,
            String moduleName

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("mty", moduleType)
                .append("mnm",moduleName)
                .append("isr", 0);
        setBaseEntry(dbObject);
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

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
