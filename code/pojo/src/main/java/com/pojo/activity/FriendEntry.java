package com.pojo.activity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * id
 * ui
 * [
 * u2,t
 * u3,
 * ]
 * u2
 * Created by yan on 2015/3/3.
 * <p>
 * <p>
 * 更改日志:2016/10/24
 * fid   friend
 * ptd   partner
 */
public class FriendEntry extends BaseDBObject {

    private static final long serialVersionUID = 1232296937168011907L;

    /*
      *
      * uid  用户id
      * fid  好友id 数组
      * */
    public FriendEntry(BasicDBObject baseDBObject) {
        super(baseDBObject);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public List<ObjectId> getFriendIds() {
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("fid");
        if (basicDBList != null) {
            for (Object object : basicDBList) {
                objectIdList.add((ObjectId) object);
            }
        }
        return objectIdList;
    }

    public List<ObjectId> getPartnerIds() {
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("ptd");
        if (basicDBList != null) {
            for (Object object : basicDBList) {
                objectIdList.add((ObjectId) object);
            }
        }
        return objectIdList;
    }
}
