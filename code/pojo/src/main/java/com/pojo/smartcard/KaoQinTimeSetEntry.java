package com.pojo.smartcard;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 一卡通中号信息
 * <pre>
 * collectionName:accountinfo
 * </pre>
 * <pre>
 {
 si:学校id
 lt(lateTime):上学时间
 mt(middleTime):中间时间
 pt(punctualTime):放学时间
 }
 * Created by guojing on 2016/5/31.
 */
public class KaoQinTimeSetEntry extends BaseDBObject {
    public KaoQinTimeSetEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public KaoQinTimeSetEntry(
            ObjectId schoolId,
            String lateTime,
            String middleTime,
            String punctualTime
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("lt", lateTime)
                .append("mt", middleTime)
                .append("pt", punctualTime);
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public String getLateTime() {
        return getSimpleStringValue("lt");
    }

    public void setLateTime(String lateTime) {
        setSimpleValue("lt", lateTime);
    }

    public String getMiddleTime() {
        return getSimpleStringValue("mt");
    }

    public void setMiddleTime(String middleTime) {
        setSimpleValue("mt", middleTime);
    }

    public String getPunctualTime() {
        return getSimpleStringValue("pt");
    }

    public void setPunctualTime(String punctualTime) {
        setSimpleValue("pt", punctualTime);
    }

}
