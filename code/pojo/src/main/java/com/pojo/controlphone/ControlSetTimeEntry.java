package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 默认控制时间表
 * Created by James on 2017/11/20.* Id
 time              控制时间              tim
 name                 名称               nam
 *
 */
public class ControlSetTimeEntry extends BaseDBObject {
    public ControlSetTimeEntry(){

    }
    public ControlSetTimeEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlSetTimeEntry(
            long time,
            String  name
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("tim", time)
                .append("nam",name)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlSetTimeEntry(
            ObjectId id,
            long time,
            String  name
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("tim", time)
                .append("nam",name)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

  public String getName(){
      return getSimpleStringValue("nam");
  }
    public void setName(String name){
        setSimpleValue("nam",name);
    }

    public long getTime(){
        return getSimpleLongValue("tim");
    }
    public void setTime(long time){
        setSimpleValue("tim",time);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
