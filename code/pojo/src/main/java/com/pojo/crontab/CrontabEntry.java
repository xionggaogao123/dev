package com.pojo.crontab;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**定时任务表
 * nm 名称
 * ver 版本
 * Created by fl on 2016/3/18.
 */
public class CrontabEntry extends BaseDBObject {

    public CrontabEntry(){}

    public CrontabEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public CrontabEntry(String name, int version){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("nm", name)
                .append("ver", version);
        setBaseEntry(baseEntry);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm", name);
    }

    public int getVersion(){
        return getSimpleIntegerValue("ver");
    }

    public void setVersion(int version){
        setSimpleValue("ver", version);
    }
}
