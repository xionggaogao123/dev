package com.pojo.fcommunity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/9/18.
 * 活动 entry
 * tid------主题id
 * uid------发布活动的用户id
 * ti-------活动标题
 * ct-------活动内容
 * st-------活动开始时间
 * et-------活动结束时间
 * crt------活动创建时间
 * lat------活动发布的精度
 * lon------活动发布的纬度
 * ip-------
 */
public class FActivityEntry extends BaseDBObject {

    public FActivityEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject) dbo);
    }

    //无法获取经纬度
    public FActivityEntry(ObjectId tid, ObjectId uid, String title,
                          String content, long startTime, long endTime, long createTime, String ip) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("tid", tid)
                .append("uid", uid)
                .append("ti", title)
                .append("ct", content)
                .append("st", startTime)
                .append("et", endTime)
                .append("crt", createTime)
                .append("ip", ip)
                .append("r", 0);
        setBaseEntry(dbo);
    }

    public FActivityEntry(ObjectId tid, ObjectId uid, String title,
                          String content, long startTime, long endTime, long createTime, double lat, double lon) {
        super();
        BasicDBObject dbo = new BasicDBObject()
                .append("tid", tid)
                .append("uid", uid)
                .append("ti", title)
                .append("ct", content)
                .append("st", startTime)
                .append("et", endTime)
                .append("crt", createTime)
                .append("lat", lat)
                .append("lon", lon)
                .append("r", 0);
        setBaseEntry(dbo);
    }

    public ObjectId getUid() {
        return getSimpleObjecIDValue("uid");
    }

    public ObjectId getTid() {
        return getSimpleObjecIDValue("tid");
    }

    public String getTitle() {
        return getSimpleStringValue("ti");
    }

    public String getContent() {
        return getSimpleStringValue("ct");
    }

    public Long getStartTime() {
        return getSimpleLongValue("st");
    }

    public Long getEndTime() {
        return getSimpleLongValue("et");
    }

    public Long getCreateTime() {
        return getSimpleLongValue("crt");
    }

    public Double getLat() {
        return getSimpleDoubleValue("lat");
    }

    public Double getLon() {
        return getSimpleDoubleValue("lon");
    }

    public String ip() {
        return getSimpleStringValue("ip");
    }

    /**
     * 参加活动的用户Id列表
     *
     * @return
     */
    public List<ObjectId> getAttendUids() {
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        if (!getBaseEntry().containsField("atd")) {
            return objectIds;
        }
        BasicDBList list = (BasicDBList) getSimpleObjectValue("atd");
        for (Object id : list) {
            objectIds.add((ObjectId) id);
        }
        return objectIds;
    }
}
