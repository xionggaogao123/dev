package com.pojo.dormitory;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/9/28.
 */
public class RoomUserEntry extends BaseDBObject {
    private static final long serialVersionUID = 2951676050689538256L;

    public RoomUserEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public RoomUserEntry(int bedNum,String userName,ObjectId userId) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("bnum", bedNum)
                .append("unm", userName)
                .append("ui", userId)
                .append("dt",System.currentTimeMillis());
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }
    public String getUserName() {
        return getSimpleStringValue("unm");
    }
    public void setUserName(String userName) {
        setSimpleValue("unm",userName);
    }
    public int getBedNum() {
        return getSimpleIntegerValue("bnum");
    }
    public void setBedNum(int bedNum) {
        setSimpleValue("bnum",bedNum);
    }
    public long getDateTime() {
        return getSimpleLongValue("dt");
    }
    public void setDateTime(long dateTime) {
        setSimpleValue("dt",dateTime);
    }
}
