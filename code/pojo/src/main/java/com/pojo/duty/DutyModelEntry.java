package com.pojo.duty;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/7/4.
 */
public class DutyModelEntry extends BaseDBObject {

    private static final long serialVersionUID = 1059785717844377204L;

    public DutyModelEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public DutyModelEntry(ObjectId schoolId,String modelName,ObjectId dutySetId) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("mnm",modelName)
                .append("dtsid",dutySetId)
                .append("ir", Constant.ZERO);
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }
    public ObjectId getDutySetId() {
        return getSimpleObjecIDValue("dtsid");
    }
    public void setDutySetId(ObjectId dutySetId) {
        setSimpleValue("dtsid", dutySetId);
    }
    public String getModelName() {
        return getSimpleStringValue("mnm");
    }
    public void setModelName(String modelName) {
        setSimpleValue("mnm", modelName);
    }
}
