package com.pojo.wrongquestion;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/6.
 * 临时年级表
 *id
 * gradeName  年级名称         gna
 * type       年级类型         typ
 * ename      年级所属         ena
 *
 */
public class CreateGradeEntry extends BaseDBObject {
    public CreateGradeEntry(){

    }
    public CreateGradeEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public CreateGradeEntry(
            String gradeName,
            int type,
            String ename
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("gna", gradeName)
                .append("typ",type)
                .append("ena",ename)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public CreateGradeEntry(
            ObjectId id,
            String gradeName,
            int type,
            String ename
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("gna", gradeName)
                .append("typ",type)
                .append("ena",ename)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public String getGradeName(){
        return getSimpleStringValue("gna");
    }

    public void setGradeName(String gradeName){
        setSimpleValue("gna",gradeName);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
    public String getEname(){
        return getSimpleStringValue("ena");
    }

    public void setEname(String ename){
        setSimpleValue("ena",ename);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
