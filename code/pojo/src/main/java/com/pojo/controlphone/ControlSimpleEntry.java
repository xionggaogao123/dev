package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

import java.util.Date;

/**
 * Created by James on 2018-06-27.
 * 单例版本控制
 * ip      执行者内网ip      ip
 * time    执行时间戳        tim
 * status  状态              sta
 * type  模块              mod     // 1 mqtt上下线消息启动
 *
 */
public class ControlSimpleEntry extends BaseDBObject {
    public ControlSimpleEntry(){

    }

    public ControlSimpleEntry(BasicDBObject object){
        super(object);
    }
    //添加构造
    public ControlSimpleEntry( String ip,
                                int status,
                                int type){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("ip",ip)
                .append("sta", status)
                .append("typ",type)
                .append("tim",new Date().getTime())
                .append("isr", 0);

        setBaseEntry(basicDBObject);
    }

    public String getIp(){
        return getSimpleStringValue("ip");
    }

    public void setIp(String ip){
        setSimpleValue("ip", ip);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getStatus(){
        return getSimpleIntegerValueDef("sta",1);
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }

    public long getTime(){
        return getSimpleLongValue("tim");
    }

    public void setTime(long time){
        setSimpleValue("tim",time);
    }
}
