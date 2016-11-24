package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/7/25.
 *
 * {
 *     id: id
 *     nm: name
 * }
 */
public class IdNamePair extends BaseDBObject {
    public IdNamePair() {
        super();
    }

    public IdNamePair(BasicDBObject baseEntry) {
        setBaseEntry(baseEntry);
    }

    public IdNamePair(ObjectId id, String name) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("id", id)
                .append("nm", name);
        setBaseEntry(baseEntry);
    }


    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }

    public void setId(ObjectId id) {
        setSimpleValue("id", id);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

}
