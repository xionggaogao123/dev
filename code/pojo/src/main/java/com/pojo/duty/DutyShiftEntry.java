package com.pojo.duty;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/7/8.
 */
public class DutyShiftEntry extends BaseDBObject {
    private static final long serialVersionUID = 191622425728951719L;

    public DutyShiftEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DutyShiftEntry(ObjectId dutyId,ObjectId schoolId,ObjectId userId,String timeDesc,String cause,int type) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("dtid", dutyId)
                .append("si",schoolId)
                .append("ui",userId)
                .append("td",timeDesc)
                .append("cause", cause)
                .append("type", type)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public ObjectId getDutyId() {
        return getSimpleObjecIDValue("dtid");
    }
    public void setDutyId(ObjectId dutyId) {
        setSimpleValue("dtid", dutyId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }
    public String getCause() {
        return getSimpleStringValue("cause");
    }
    public void setCause(String cause) {
        setSimpleValue("cause",cause);
    }
    public int getType() {
        return getSimpleIntegerValue("type");
    }
    public void setType(int type) {
        setSimpleValue("type",type);
    }
    public String getTimeDesc() {
        return getSimpleStringValue("td");
    }
    public void setTimeDesc(String timeDesc) {
        setSimpleValue("td",timeDesc);
    }
}
