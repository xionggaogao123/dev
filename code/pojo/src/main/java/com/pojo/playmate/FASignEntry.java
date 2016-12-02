package com.pojo.playmate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by moslpc on 2016/12/2.
 * 报名人数---报名表
 */
public class FASignEntry extends BaseDBObject {

    public FASignEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject) dbo);
    }

    public FASignEntry(ObjectId _id, ObjectId acid, ObjectId uid, String signText) {
        BasicDBObject dbo = new BasicDBObject()
                .append("_id", _id)
                .append("acid", acid)
                .append("uid", uid)
                .append("sitx", signText)
                .append("ti", System.currentTimeMillis());
        setBaseEntry(dbo);
    }

    public ObjectId getAcid() {
        return getSimpleObjecIDValue("acid");
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public String getSignText() {
        return getSimpleStringValue("sitx");
    }

}
