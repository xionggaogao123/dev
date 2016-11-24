package com.fulaan.pojo;

import org.bson.types.ObjectId;

/**
 * Created by jerry on 2016/9/19.
 * 位置 Location
 */
public class Location {

    private ObjectId uid;
    private Double lat;
    private Double lon;

    public org.bson.types.ObjectId getUid() {
        return uid;
    }

    public void setUid(org.bson.types.ObjectId uid) {
        this.uid = uid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
