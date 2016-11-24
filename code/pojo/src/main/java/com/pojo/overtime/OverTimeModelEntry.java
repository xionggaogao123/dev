package com.pojo.overtime;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/7/15.
 */
public class OverTimeModelEntry extends BaseDBObject {

    private static final long serialVersionUID = 1698303419082388782L;

    public OverTimeModelEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public OverTimeModelEntry(ObjectId schoolId,String modelName,ObjectId applicantUserId,ObjectId jbUserId,long date,String startTime,String endTime,String cause,ObjectId auditUserId) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si",schoolId)
                .append("mn",modelName)
                .append("apuid", applicantUserId)
                .append("juid", jbUserId)
                .append("date",date)
                .append("st",startTime)
                .append("et", endTime)
                .append("cause", cause)
                .append("atuid", auditUserId)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public ObjectId getDutyTimeId() {
        return getSimpleObjecIDValue("dtid");
    }
    public void setApplicantUserId(ObjectId applicantUserId) {
        setSimpleValue("apuid",applicantUserId);
    }
    public ObjectId getApplicantUserId() {
        return getSimpleObjecIDValue("apuid");
    }
    public void setJbUserId(ObjectId jbUserId) {
        setSimpleValue("juid",jbUserId);
    }
    public ObjectId getJbUserId() {
        return getSimpleObjecIDValue("juid");
    }
    public void setDate(long date) {
        setSimpleValue("date",date);
    }
    public long getDate() {
        return getSimpleLongValue("date");
    }
    public void setAuditUserId(ObjectId auditUserId) {
        setSimpleValue("atuid",auditUserId);
    }
    public ObjectId getAuditUserId() {
        return getSimpleObjecIDValue("atuid");
    }
    public void setStartTime(String startTime) {
        setSimpleValue("st",startTime);
    }
    public String getStartTime() {
        return getSimpleStringValue("st");
    }
    public void setEndTime(String endTime) {
        setSimpleValue("et",endTime);
    }
    public String getEndTime() {
        return getSimpleStringValue("et");
    }
    public void setCause(String cause) {
        setSimpleValue("cause",cause);
    }
    public String getCause() {
        return getSimpleStringValue("cause");
    }
    public void setModelName(String modelName) {
        setSimpleValue("mn",modelName);
    }
    public String getModelName() {
        return getSimpleStringValue("mn");
    }

}
