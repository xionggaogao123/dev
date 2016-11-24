package com.pojo.duty;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 *
 * Created by wang_xinxin on 2016/6/29.
 */
public class DutyTimeEntry extends BaseDBObject {
    private static final long serialVersionUID = -9110924209269865094L;

    public DutyTimeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public DutyTimeEntry(ObjectId dutyId,String timeDesc,String startTime,String endTime) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("dtid",dutyId)
                .append("tdesc", timeDesc)
                .append("st", startTime)
                .append("et", endTime)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }
    public void setDutyId(ObjectId dutyId) {
        setSimpleValue("dtid", dutyId);
    }
    public ObjectId getDutyId() {
        return getSimpleObjecIDValue("dtid");
    }
    public String getTimeDesc() {
        return getSimpleStringValue("tdesc");
    }
    public void setTimeDesc(String timeDesc) {
        setSimpleValue("tdesc", timeDesc);
    }
    public String getStartTime() {
        return getSimpleStringValue("st");
    }
    public void setStartTime(String startTime) {
        setSimpleValue("st", startTime);
    }
    public String getEndTime() {
        return getSimpleStringValue("et");
    }
    public void setEndTime(String endTime) {
        setSimpleValue("et", endTime);
    }
}
