package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/10/18.
 */
public class GroupExamVersionEntry extends BaseDBObject{

    public GroupExamVersionEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public GroupExamVersionEntry(ObjectId groupExamDetailId,
                                 long version){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("v",version)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getGroupExamDetailId(){
        return getSimpleObjecIDValue("eid");
    }

    public void setGroupExamDetailId(ObjectId groupExamDetailId){
        setSimpleValue("eid",groupExamDetailId);
    }

    public long getVersion(){
        return getSimpleLongValueDef("v",1L);
    }

    public void setVersion(long version){
        setSimpleValue("v",version);
    }
}
