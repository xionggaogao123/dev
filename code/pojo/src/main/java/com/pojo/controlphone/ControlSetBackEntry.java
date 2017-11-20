package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/20.
 *
 * appTime            应用回调时间             atm
backTime              地图回调时间              btm
 type                 1 默认配置            typ

 */
public class ControlSetBackEntry extends BaseDBObject{
    public ControlSetBackEntry(){

    }
    public ControlSetBackEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlSetBackEntry(
            long backTime,
            long appTime,
            int  type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("btm", backTime)
                .append("atm", appTime)
                .append("typ",type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlSetBackEntry(
            ObjectId id,
            long backTime,
            long appTime,
            int  type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("btm", backTime)
                .append("atm", appTime)
                .append("typ",type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }
    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public long getBacktime(){
        return getSimpleLongValue("btm");
    }
    public void setBackTime(long backTime){
        setSimpleValue("btm",backTime);
    }
    public long getAppTime(){
        return getSimpleLongValue("atm");
    }
    public void setAppTime(long appTime){
        setSimpleValue("atm",appTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
