package com.pojo.jiaschool;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/2/2.
 */
public class SchoolCommunityEntry extends BaseDBObject {

    public SchoolCommunityEntry(){

    }

    public SchoolCommunityEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public SchoolCommunityEntry(
            ObjectId communityId,
            ObjectId schoolId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("cid", communityId)
                .append("sid", schoolId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SchoolCommunityEntry(
            ObjectId id,
            ObjectId communityId,
            ObjectId schoolId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("cid", communityId)
                .append("sid", schoolId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
