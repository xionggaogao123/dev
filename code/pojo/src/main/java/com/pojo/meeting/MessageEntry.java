package com.pojo.meeting;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/8/17.
 */
public class MessageEntry extends BaseDBObject {
    private static final long serialVersionUID = -3381192942470566801L;

    public MessageEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MessageEntry(ObjectId meetId,ObjectId schoolId,ObjectId userId,long time,String content) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("mid", meetId)
                .append("si", schoolId)
                .append("ui", userId)
                .append("time",time)
                .append("con",content);
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public ObjectId getMeetId() {
        return getSimpleObjecIDValue("mid");
    }
    public void setMeetId(ObjectId meetId) {
        setSimpleValue("mid", meetId);
    }
    public String getContent() {
        return getSimpleStringValue("con");
    }
    public void setContent(String content) {
        setSimpleValue("con", content);
    }
    public long getTime() {
        return getSimpleLongValue("time");
    }
    public void setTime(long time) {
        setSimpleValue("time", time);
    }
}
