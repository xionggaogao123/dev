package com.pojo.mobile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by jerry on 2017/1/4.
 * 用户手机Entry
 * uid：用户id
 * mobs: 用户绑定手机号
 */
public class UserMobileEntry extends BaseDBObject {

    public UserMobileEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject) dbo);
    }

    public UserMobileEntry(String mobile, ObjectId userId) {
        BasicDBObject dbo = new BasicDBObject()
                .append("mob", mobile)
                .append("uids", new ObjectId[]{userId});
        setBaseEntry(dbo);
    }

    public UserMobileEntry(String mobile, List<ObjectId> userIds) {
        BasicDBObject dbo = new BasicDBObject()
                .append("mob", mobile)
                .append("uids", userIds);
        setBaseEntry(dbo);
    }

    public String getMobile() {
        return getSimpleStringValue("mob");
    }

    public List<ObjectId> getUserIds() {
        return MongoUtils.getObjectIdListByDBList(getSimpleObjectValue("uids"));
    }
}
