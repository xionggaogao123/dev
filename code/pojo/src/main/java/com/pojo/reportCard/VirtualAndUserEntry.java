package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-03-29.
 */
public class VirtualAndUserEntry extends BaseDBObject {


    public VirtualAndUserEntry(){

    }
    public VirtualAndUserEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public VirtualAndUserEntry(
            ObjectId communityId,
            ObjectId virtualId,
            ObjectId userId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("cid", communityId)
                .append("vid", virtualId)
                .append("uid", userId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public VirtualAndUserEntry(
            ObjectId id,
            ObjectId communityId,
            ObjectId virtualId,
            ObjectId userId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("cid", communityId)
                .append("vid",virtualId)
                .append("uid", userId)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getVirtualId(){
        return getSimpleObjecIDValue("vid");
    }
    public void setVirtualId(ObjectId virtualId){
        setSimpleValue("vid",virtualId);
    }



    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
