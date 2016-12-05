package com.pojo.playmate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by moslpc on 2016/12/5.
 */
public class MateTagEntry extends BaseDBObject {

    public MateTagEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject)dbo);
    }


}
