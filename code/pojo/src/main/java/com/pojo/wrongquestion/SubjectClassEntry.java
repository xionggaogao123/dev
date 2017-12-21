package com.pojo.wrongquestion;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 学科表
 * Created by James on 2017/9/6.
 * id
 * subjectId     学科id        sid
 * name          学科名称      nam
 * imageUrl      学科图标       img
 *
 *
 */
public class SubjectClassEntry extends BaseDBObject {
    public SubjectClassEntry(){

    }
    public SubjectClassEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public SubjectClassEntry(
            ObjectId subjectId,
            String name,
            String imageUrl
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", subjectId)
                .append("nam", name)
                .append("img",imageUrl)
                .append("lev",0)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SubjectClassEntry(
            ObjectId id,
            ObjectId subjectId,
            String name,
            String imageUrl
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("sid", subjectId)
                .append("nam", name)
                .append("img",imageUrl)
                .append("lev", 0)
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
    public String getImageUrl(){
        return getSimpleStringValue("nam");
    }

    public void setImageUrl(String imageUrl){
        setSimpleValue("nam",imageUrl);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public int getLevel(){
        return getSimpleIntegerValue("lev");
    }

    public void setLevel(int level){
        setSimpleValue("lev",level);
    }
}
