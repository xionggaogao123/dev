package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by jerry on 2016/9/19.
 * 位置Entry
 * uid-----------用户id
 * lat-----------经度
 * lon-----------纬度
 * ip------------ip地址
 * ti------------时间
 */
public class LocationEntry extends BaseDBObject {

    public LocationEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public LocationEntry(ObjectId uid, double lat, double lon, String ip, long time) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("uid", uid)
                .append("lat", lat)
                .append("lon", lon)
                .append("ip", ip)
                .append("ti", time);
        setBaseEntry(dbo);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public double getLat() {
        return getSimpleDoubleValue("lat");
    }

    public double getLon() {
        return getSimpleDoubleValue("lon");
    }

    public String getIp() {
        return getSimpleStringValue("ip");
    }

    public long getTime() {
        return getSimpleLongValue("ti");
    }

    @Override
    public String toString() {
        return "LocationEntry{" + getUserId() + getLat() + getLon() + getIp() + getTime() + "}";
    }
}
