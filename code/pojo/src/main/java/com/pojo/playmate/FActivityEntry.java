package com.pojo.playmate;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 * 活动表
 * acode - 活动code
 * title  - 标题
 * description - 详情
 * lon ----经度
 * lat ---- 维度
 * tags ----所属的标签
 * startTime --- 开始时间
 * endTime ----- 结束时间
 */
public class FActivityEntry extends BaseDBObject {

    public FActivityEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject) dbo);
    }

    public FActivityEntry(ObjectId _id,ObjectId uid,int acode,String title,String description,double lon,double lat,long activityTime){
        List<Double> locations = new ArrayList<Double>();
        locations.add(lon);
        locations.add(lat);
        BasicDBObject gemObject = new BasicDBObject("type", "Point").append("coordinates", locations);
        BasicDBObject dbo = new BasicDBObject()
                .append("_id",_id)
                .append("uid",uid)
                .append("acd",acode)
                .append("til",title)
                .append("des",description)
                .append("st",System.currentTimeMillis())
                .append("act",activityTime)
                .append("loc",gemObject);
        setBaseEntry(dbo);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public int getACode(){
        return getSimpleIntegerValue("acd");
    }

    public String getTitle(){
        return getSimpleStringValue("til");
    }

    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public long getCreateTime(){
        return getSimpleLongValue("st");
    }

    public long getActivityTime(){
        return getSimpleLongValue("act");
    }

    public BasicDBList getLocations(){
        DBObject dbo = (DBObject)getSimpleObjectValue("loc");
        return dbo == null ? null : (BasicDBList) dbo.get("coordinates");
    }
}
