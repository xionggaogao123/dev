package com.pojo.controlphone;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

public class AppTsEntry extends BaseDBObject{

    

    public AppTsEntry(BasicDBObject baseEntry) {
        super(baseEntry);
        // TODO Auto-generated constructor stub
    }

    public AppTsEntry() {
        BasicDBObject dbObject = new BasicDBObject()
            .append("ti", System.currentTimeMillis());
        setBaseEntry(dbObject);
    }
    
    public AppTsEntry(ObjectId cmid) {
        BasicDBObject dbObject = new BasicDBObject().append("cmid", cmid)
            .append("ti", System.currentTimeMillis());
        setBaseEntry(dbObject);
    }
}
