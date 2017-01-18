package com.fulaan.user.model;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by jerry on 2016/8/2.
 * <p>
 * 第三方登录Entry类
 * collectionName:thirdLogin
 * type: 第三方登录类型 1.微信  2.QQ
 * oid: open Id,第三方的唯一标示
 * uid: 用户的id
 */
public class ThirdLoginEntry extends BaseDBObject {

    public ThirdLoginEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ThirdLoginEntry(ObjectId uid, String oid, String unionid, ThirdType type) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid", uid)
                .append("oid", oid)
                .append("unionid", unionid)
                .append("type", type.getCode());
        setBaseEntry(baseEntry);

    }

    public ObjectId getUid() {
        return getSimpleObjecIDValue("uid");
    }
}
