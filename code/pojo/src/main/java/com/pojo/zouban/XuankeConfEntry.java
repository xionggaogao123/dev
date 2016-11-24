package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 选课配置
 * {
 * sid:学校id---------------->schoolId
 * te:学期------------------->term
 * gid:年级id---------------->gradeId
 * stadt:选课开始时间--------------->startDate
 * eddt:选课结束时间--------------->endDate
 *
 * isrels:是否开始选课------------>isRelease      1: 已开始 0：未开始
 *
 * info:选课的一些说明
 *
 * //新增字段
 * end:结束选课--------------->end  1: 选课已结束  0 ： 选课未结束
 *
 * //已废弃
 * exid:考试id------------->examId
 *
 * }
 * Created by wang_xinxin on 2015/9/21.
 */
public class XuankeConfEntry extends BaseDBObject {

    public XuankeConfEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public XuankeConfEntry(ObjectId schoolId, String term, ObjectId gradeId) {
        this(schoolId, term, gradeId, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public XuankeConfEntry(ObjectId schoolId, String term, ObjectId gradeId, long startDate, long endDate) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("te", term)
                .append("gid", gradeId)
                .append("stadt", startDate)
                .append("eddt", endDate)
                .append("isrels", 0)
                .append("end", 0)
                .append("info", "");
        setBaseEntry(baseEntry);

    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public String getTerm() {
        return getSimpleStringValue("te");
    }

    public void setTerm(String term) {
        setSimpleValue("te", term);
    }

    public long getStartDate() {
        return getSimpleLongValue("stadt");
    }

    public void setStartDate(long startDate) {
        setSimpleValue("stadt", startDate);
    }

    public long getEndDate() {
        return getSimpleLongValue("eddt");
    }

    public void setEndDate(long endDate) {
        setSimpleValue("eddt", endDate);
    }

    public int getIsRelease() {
        return getSimpleIntegerValue("isrels");
    }

    public void setIsRelease(int isRelease) {
        setSimpleValue("isrels", isRelease);
    }

    public int getEnd() {
        if (getBaseEntry().containsField("end")) {
            return getSimpleIntegerValue("end");
        }

        return 1;
    }

    public void setEnd(int end) {
        setSimpleValue("end", end);
    }

    public String getInfo(){
        return getSimpleStringValueDef("info", "");
    }

    public void setInfo(String info){
        setSimpleValue("info", info);
    }
}
