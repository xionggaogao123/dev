package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-09-27.
 *
 * 校管控内默认配置应用设置表（针对放学后）
 *
 * appId                    应用id              aid
 * appPackageName           包名                apn
 * controlType              管控类型            cty     1 受管控  2 不受管控
 * freeTime                 开放时间            ftm      （毫秒值）
 * type                     生效阶段            typ     0 放学后
 *
 */
public class ControlAppSchoolEntry extends BaseDBObject {
    public ControlAppSchoolEntry(){

    }
    public ControlAppSchoolEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ControlAppSchoolEntry(ObjectId appId,
                                 String appPackageName,
                                 int controlType,
                                 long freeTime,
                                 int type){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("aid",appId)
                .append("apn",appPackageName)
                .append("cty",controlType)
                .append("ftm",freeTime)
                .append("typ",type)
                .append("ti", System.currentTimeMillis())
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getAppId(){
        return getSimpleObjecIDValue("aid");
    }

    public void setAppId(ObjectId appId){
        setSimpleValue("aid",appId);
    }

    public void setAppPackageName(String appPackageName){
        setSimpleValue("apn",appPackageName);
    }

    public String getAppPackageName(){
        return getSimpleStringValue("apn");
    }

    public void setFreeTime(long freeTime){
        setSimpleValue("ftm",freeTime);
    }

    public long getFreeTime(){
        return getSimpleLongValue("ftm");
    }

    public void setControlType(int controlType){
        setSimpleValue("cty",controlType);
    }

    public int getControlType(){
        return getSimpleIntegerValue("cty");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }


    public void setTime(long time){
        setSimpleValue("ti",time);
    }

    public long getTime(){
        return getSimpleLongValue("ti");
    }


}
