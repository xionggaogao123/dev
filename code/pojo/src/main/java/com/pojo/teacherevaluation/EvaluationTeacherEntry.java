package com.pojo.teacherevaluation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**教师评价教师基础信息
 * sid 学校id
 * tid 老师id
 * stat 个人陈述
 * evi 实证资料
 * Created by fl on 2016/9/12.
 */
public class EvaluationTeacherEntry extends BaseDBObject {

    public EvaluationTeacherEntry(){}

    public EvaluationTeacherEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public EvaluationTeacherEntry(ObjectId schoolId, ObjectId teacherId, String statement, String evidence ){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", schoolId)
                .append("tid", teacherId)
                .append("stat", statement)
                .append("evi", evidence)
                ;
        setBaseEntry(baseEntry);
    }


    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }

    public String getStatement() {
        return getSimpleStringValue("stat");
    }

    public void setStatement(String statement) {
        setSimpleValue("stat", statement);
    }

    public String getEvidence() {
        return getSimpleStringValue("evi");
    }

    public void setEvidence(String evidence) {
        setSimpleValue("evi", evidence);
    }

}
