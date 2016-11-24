package com.pojo.smartcard;

/**
 * Created by guojing on 2016/6/20.
 */

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
 uid:用户id
 ty:统计类型:bz(本周),by(本月),xq(学期)
 nc:正常次数
 lc:迟到次数
 pc:早退次数
 kc:旷课次数
 }

 int normalCount=0;
 int lateCount=0;
 int punctualCount=0;
 int kuangkeCount=0;
 * Created by guojing on 2016/5/31.
 */
public class KaoQinStateEntry extends BaseDBObject {
    public KaoQinStateEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public KaoQinStateEntry(
            ObjectId userId,
            String type,
            int normalCount,
            int lateCount,
            int punctualCount,
            int kuangkeCount
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("uid", userId)
                .append("ty", type)
                .append("nc", normalCount)
                .append("lc", lateCount)
                .append("pc", punctualCount)
                .append("kc", kuangkeCount);
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("uid", userId);
    }

    public String getType() {
        return getSimpleStringValue("ty");
    }

    public void setType(String type) {
        setSimpleValue("ty", type);
    }

    public int getNormalCount() {
        return getSimpleIntegerValue("nc");
    }

    public void setNormalCount(int normalCount) {
        setSimpleValue("nc", normalCount);
    }

    public int getLateCount() {
        return getSimpleIntegerValue("lc");
    }

    public void setLateCount(int lateCount) {
        setSimpleValue("lc", lateCount);
    }

    public int getPunctualCount() {
        return getSimpleIntegerValue("pc");
    }

    public void setPunctualCount(int punctualCount) {
        setSimpleValue("pc", punctualCount);
    }

    public int getKuangkeCount() {
        return getSimpleIntegerValue("kc");
    }

    public void setKuangkeCount(int kuangkeCount) {
        setSimpleValue("kc", kuangkeCount);
    }
}
