package com.pojo.playmate;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by moslpc on 2016/11/30.
 * 玩伴 -- 分页 Entry
 * uid -- 用户id
 * loc -- 地理位置 [lat,lon]
 * hob -- 爱好 [篮球，足球]
 * tag -- 标签 [学生，二次元]
 * ti  -- 最近一次登录时间
 * aged -- 年龄段[  3-5: 1 ,5-8: 2,8-11: 3,11-15~ 4,15-18:5 18~:6 ] - -1未知
 * ons -- 在线时间段 [ 00:00 - 06:00 : 1,06:00-12:00 : 2,12:00- 18:00 : 3,18:00 - 24:00 :4] - -1未知
 */
public class FMateEntry  extends BaseDBObject{

    public FMateEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject) dbo);
    }

    public FMateEntry(ObjectId _id,ObjectId userId) {
        BasicDBObject dbo = new BasicDBObject()
                .append(Constant.ID,_id)
                .append("uid", userId)
                .append("loc", Constant.DEFAULT_VALUE_OBJECT)
                .append("hob", Constant.DEFAULT_VALUE_OBJECT)
                .append("tag", Constant.DEFAULT_VALUE_ARRAY)
                .append("ti", System.currentTimeMillis())
                .append("aged",Constant.DEFAULT_VALUE_INT)
                .append("ons", Constant.DEFAULT_VALUE_INT);
        setBaseEntry(dbo);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public BasicDBList getLocation() {
        DBObject dbo = (DBObject)getSimpleObjectValue("loc");
        if(dbo == null) return null;
        return (BasicDBList) dbo.get("coordinates");
    }

    public BasicDBList getHobbys() {
        return (BasicDBList)getSimpleObjectValue("hob");
    }

    public BasicDBList getTags() {
        return (BasicDBList)getSimpleObjectValue("tag");
    }

    public long getTime() {
        return getSimpleLongValue("ti");
    }

    public int getAged() {
        return getSimpleIntegerValue("aged");
    }

    public int getOns() {
        return getSimpleIntegerValue("ons");
    }

}
