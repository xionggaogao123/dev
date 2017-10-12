package com.pojo.wrongquestion;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 临时问题类型表
 * Created by James on 2017/10/12.
 * id                id          id
 * name             名称         nam
 * subjectId        学科id       sid
 * sename           阶段名       ena
 */
public class QuestionTypeEntry extends BaseDBObject {
    public QuestionTypeEntry(){

    }
    public QuestionTypeEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public QuestionTypeEntry(
            ObjectId subjectId,
            String name,
            String sename
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", subjectId)
                .append("nam", name)
                .append("ena",sename)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public QuestionTypeEntry(
            ObjectId id,
            ObjectId subjectId,
            String name,
            String sename
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("sid", subjectId)
                .append("nam", name)
                .append("ena",sename)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
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
