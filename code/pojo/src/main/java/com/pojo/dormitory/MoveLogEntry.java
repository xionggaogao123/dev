package com.pojo.dormitory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/9/27.
 */
public class MoveLogEntry extends BaseDBObject {
    private static final long serialVersionUID = 6331293533882890249L;
    public MoveLogEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MoveLogEntry(ObjectId roomId,String userName,int bedNum,ObjectId userId,String cause,String dormName,String loopName,String roomName) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("rid", roomId)
                .append("bum", bedNum)
                .append("unm", userName)
                .append("uid", userId)
                .append("cause",cause)
                .append("dnm",dormName)
                .append("lnm",loopName)
                .append("rnm",roomName)
                .append("dt", System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }
    public ObjectId getRoomId() {
        return getSimpleObjecIDValue("rid");
    }
    public void setRoomId(ObjectId roomId) {
        setSimpleValue("rid", roomId);
    }
    public String getUserName() {
        return getSimpleStringValue("unm");
    }
    public void setUserName(String userName) {
        setSimpleValue("unm",userName);
    }
    public int getBedNum() {
        return getSimpleIntegerValue("bum");
    }
    public void setBedNum(int bedNum) {
        setSimpleValue("bum",bedNum);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }
    public String getCause() {
        return getSimpleStringValue("cause");
    }
    public void setCause(String cause) {
        setSimpleValue("cause",cause);
    }
    public long getTime() {
        return getSimpleLongValue("dt");
    }
    public void setTime(long time) {
        setSimpleValue("dt",time);
    }
    public String getDormName() {
        return getSimpleStringValue("dnm");
    }
    public void setDormName(String dormName) {
        setSimpleValue("dnm",dormName);
    }
    public String getLoopName() {
        return getSimpleStringValue("lnm");
    }
    public void setLoopName(String loopName) {
        setSimpleValue("lnm",loopName);
    }
    public String getRoomName() {
        return getSimpleStringValue("rnm");
    }
    public void setRoomName(String roomName) {
        setSimpleValue("rnm",roomName);
    }
}
