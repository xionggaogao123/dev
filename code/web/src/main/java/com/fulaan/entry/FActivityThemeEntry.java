package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by jerry on 2016/9/21.
 * 活动主题
 * nm--------主题名
 * r---------状态  r=0正常状态  r=1删除状态
 */
public class FActivityThemeEntry extends BaseDBObject {

    public FActivityThemeEntry(DBObject dbo) {
        super((BasicDBObject) dbo);
    }

    public FActivityThemeEntry(String name) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("nm", name)
                .append("r", 0);
        setBaseEntry(dbo);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

}
