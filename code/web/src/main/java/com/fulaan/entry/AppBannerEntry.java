package com.fulaan.entry;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by moslpc on 2016/9/6.
 * ss: status: 状态，1，上线   2，下线 默认是2
 * nm---------商品名字
 * il---------指向url
 * gd---------商品id
 * ss---------banner状态  1，上线  2，下线
 * ct---------创建时间
 */
public class AppBannerEntry extends BaseDBObject {

    public AppBannerEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public AppBannerEntry(String imageUrl, String goodName, ObjectId goodId, int status) {
        super();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("nm", goodName);
        dbo.append("il", imageUrl);
        dbo.append("gd", goodId);
        dbo.append("ss", status);
        dbo.append("ct", System.currentTimeMillis());
        setBaseEntry(dbo);
    }

    public String getImagUrl() {
        return getSimpleStringValue("il");
    }

    public ObjectId getGoodId() {
        return getSimpleObjecIDValue("gd");
    }

    public int getStatus() {
        return getSimpleIntegerValue("ss");
    }

    public long getCreateTime() {
        return getComplexLongValue("ct");
    }

    public String getGoodName() {
        return getSimpleStringValue("nm");
    }
}
