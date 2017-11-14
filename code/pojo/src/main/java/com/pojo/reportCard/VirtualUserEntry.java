package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/13.
 * 虚拟账户表
 */
public class VirtualUserEntry extends BaseDBObject{

    public VirtualUserEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public VirtualUserEntry(ObjectId communityId,
                            ObjectId groupId){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("cid",communityId)
                .append("gid",groupId);
        setBaseEntry(basicDBObject);
    }
}
