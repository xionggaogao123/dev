package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

/**
 * 班级排名
 * {
 * exid:考试id------------>examId
 * sid:学校id---------------->schoolId
 * gid:年级id-------------->gradeId
 * cid:班级id------------------->classId
 * sum:成绩---------------->sum
 * }
 * Created by wang_xinxin on 2015/10/13.
 */
public class GradeClassSoreEntry extends BaseDBObject {

    public GradeClassSoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public GradeClassSoreEntry(ObjectId examId,ObjectId schoolId,ObjectId gradeId,ObjectId classId,int sum) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("exid",examId)
                .append("sid", schoolId)
                .append("gid",gradeId)
                .append("cid", classId)
                .append("sum",sum);
        setBaseEntry(baseEntry);
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exid");
    }

    public void setExamId(ObjectId examId) {
        setSimpleValue("exid",examId);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid",gradeId);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cid",classId);
    }

    public int getSum() {
        return getSimpleIntegerValue("sum");
    }

    public void setSum(int sum) {
        setSimpleValue("sum",sum);
    }
}
