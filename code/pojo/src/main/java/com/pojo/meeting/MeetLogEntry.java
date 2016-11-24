package com.pojo.meeting;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/8/16.
 */
public class MeetLogEntry extends BaseDBObject {
    private static final long serialVersionUID = 3805839314504068810L;

    public MeetLogEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MeetLogEntry(ObjectId userId, ObjectId schoolId,ObjectId meetId,String name,String issue,long openTime,long endTime ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("ui", userId)
                .append("si", schoolId)
                .append("mid", meetId)
                .append("nm", name)
                .append("issue", issue)
                .append("ot", openTime)
                .append("et", endTime)
                .append("ir", Constant.ZERO);
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
    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }
    public String getIssue() {
        return getSimpleStringValue("issue");
    }
    public void setIssue(String issue) {
        setSimpleValue("issue", issue);
    }
    public long getOpenTime() {
        return getSimpleLongValue("ot");
    }
    public void setOpenTime(long openTime) {
        setSimpleValue("ot", openTime);
    }
    public long getEndTime() {
        return getSimpleLongValue("et");
    }
    public void setEndTime(long endTime) {
        setSimpleValue("et", endTime);
    }
}
