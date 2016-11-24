package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 *<pre>
 * collectionName:classOverallQualityScore
 * </pre>
 * <pre>
 {
 si:学校ID
 gi:年级ID
 ci:班级ID
 ui:用户ID
 gsi:商品ID
 hui:处理人Id
 ht:处理时间
 rc:拒绝理由
 cs:状态
 }
 * Created by guojing on 2016/8/24.
 */
public class StuChangeGoodsEntry extends BaseDBObject {

    public StuChangeGoodsEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public StuChangeGoodsEntry(
            ObjectId schoolId,
            ObjectId gradeId,
            ObjectId classId,
            ObjectId userId,
            ObjectId goodsId,
            ObjectId handleUserId,
            long handleTime,
            ChangeState state
    ) {
        BasicDBObject baseEntry =new BasicDBObject()
                .append("si", schoolId)
                .append("gi", gradeId)
                .append("ci", classId)
                .append("ui", userId)
                .append("gsi", goodsId)
                .append("hui", handleUserId)
                .append("ht", handleTime)
                .append("cs", state.getState());
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gi");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gi", gradeId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("ci");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("ci", classId);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public ObjectId getGoodsId() {
        return getSimpleObjecIDValue("gsi");
    }

    public void setGoodsId(ObjectId goodsId) {
        setSimpleValue("gsi", goodsId);
    }

    public ObjectId getHandleUserId() {
        return getSimpleObjecIDValue("hui");
    }

    public void setHandleUserId(ObjectId handleUserId) {
        setSimpleValue("hui", handleUserId);
    }

    public long getHandleTime() {
        return getSimpleLongValue("ht");
    }

    public void setHandleTime(long handleTime) {
        setSimpleValue("ht",handleTime);
    }

    public String getRefuseCon() {
        return getSimpleStringValue("rc");
    }

    public void setRefuseCon(String refuseCon) {
        setSimpleValue("rc", refuseCon);
    }

    public int getChangeState() {
        return getSimpleIntegerValueDef("cs",0);
    }

    public void setChangeState(int changeState) {
        setSimpleValue("cs", changeState);
    }
}
