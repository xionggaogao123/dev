package com.pojo.playmate;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * Created by moslpc on 2016/12/5.
 */
public class FMateTypeEntry extends BaseDBObject {

    public FMateTypeEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject)dbo);
    }

    public FMateTypeEntry(int type) {
        BasicDBObject dbo = new BasicDBObject("ty",type).append("data", Constant.DEFAULT_VALUE_ARRAY);
        setBaseEntry(dbo);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public BasicDBList getData() {
        return (BasicDBList)getSimpleObjectValue("data");
    }


}
