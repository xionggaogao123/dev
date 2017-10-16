package com.pojo.wrongquestion;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 临时测试类型表
 * Created by James on 2017/10/12.
 * id
 * name            名称
 * sename           阶段名       ena
 */
public class TestTypeEntry extends BaseDBObject {
    public TestTypeEntry(){

    }
    public TestTypeEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public TestTypeEntry(
            String name,
            String sename
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("nam", name)
                .append("ena", sename)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public TestTypeEntry(
            ObjectId id,
            String name,
            String sename
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("nam", name)
                .append("ena", sename)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public String getName(){
        return getSimpleStringValue("nam");
    }

    public void setName(String name){
        setSimpleValue("nam",name);
    }
    public String getSename(){
        return getSimpleStringValue("ena");
    }

    public void setSename(String sename){
        setSimpleValue("ena",sename);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
