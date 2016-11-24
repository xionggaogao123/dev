package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.omg.CORBA.*;

/**
 * 选课配置
 * {
 * sid:学校id---------------->schoolId
 * te:学期------------------->term
 * gid:年级id---------------->gradeId
 * advcnt:等级考数量---------------->advanceCount
 * sipcnt:合格考数量-------------->simpleCount
 * stadt:发布开始时间--------------->startDate
 * eddt:发布结束时间--------------->endDate
 * ispub:是否公示--------------->isPublic
 * isrels:是否发布------------>isRelease
 * exid:考试id------------->examId
 * clscnt:每段学科教学班数量 -------->classCount
 * dflg:删除------------>delflg
 * }
 * Created by wang_xinxin on 2015/9/21.
 */
public class XuankeConfEntry extends BaseDBObject {

    public XuankeConfEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public XuankeConfEntry(ObjectId schoolId,String team,ObjectId gradeId) {
        this(schoolId,team,gradeId,Constant.ZERO,Constant.ZERO,Constant.ZERO,Constant.ZERO, Constant.ZERO,Constant.ZERO,Constant.ZERO,Constant.ZERO,null);
    }

    public XuankeConfEntry(ObjectId schoolId,String term,ObjectId gradeId,int advanceCount,int simpleCount,long startDate,long endDate,int delflg,int isPublic,int isRelease,int classCount,ObjectId examId) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("sid",schoolId)
                .append("te", term)
                .append("gid",gradeId)
                .append("advcnt",advanceCount)
                .append("sipcnt",simpleCount)
                .append("stadt",startDate)
                .append("eddt",endDate)
                .append("dflg",delflg)
                .append("ispub",isPublic)
                .append("isrels",isRelease)
                .append("clscnt",classCount)
                .append("exid",examId);
        setBaseEntry(baseEntry);

    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exid");
    }

    public void setExamId(ObjectId examId) {
        setSimpleValue("exid",examId);
    }


    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }
    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid",gradeId);
    }

    public String getTeam() {
        return getSimpleStringValue("te");
    }

    public void setTeam(String team) {
        setSimpleValue("te",team);
    }

    public int getAdvanceCount() {
        return getSimpleIntegerValue("advcnt");
    }

    public void setAdvanceCount(int advanceCount) {
        setSimpleValue("advcnt",advanceCount);
    }

    public int getSimpleCount() {
        return getSimpleIntegerValue("sipcnt");
    }

    public void setSimpleCount(int simpleCount) {
        setSimpleValue("sipcnt",simpleCount);
    }

    public long getStartDate() {
        return getSimpleLongValue("stadt");
    }

    public void setStartDate(long startDate) {
        setSimpleValue("stadt",startDate);
    }

    public long getEndDate() {
        return getSimpleLongValue("eddt");
    }

    private void setEndDate(long endDate) {
        setSimpleValue("eddt",endDate);
    }

    public int getDelflg() {
        return getSimpleIntegerValue("dflg");
    }

    public void setDelflg(int delflg) {
        setSimpleValue("dflg",delflg);
    }

    public int getIsPublic() {
        return getSimpleIntegerValue("ispub");
    }

    public void setIsPublic(int isPublic) {
        setSimpleValue("ispub",isPublic);
    }

    public int getIsRelease() {
        return getSimpleIntegerValue("isrels");
    }

    public void setIsRelease(int isRelease) {
        setSimpleValue("isrels",isRelease);
    }

    public int getClassCount() {
        return getSimpleIntegerValue("clscnt");
    }

    public void setClassCount(int classCount) {
        setSimpleValue("clscnt",classCount);
    }
}
