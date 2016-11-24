package com.pojo.examscoreanalysis;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**新成绩分析 成绩表
 * Created by fl on 2016/8/12.
 * exid 考试id
 * stuid 学生id
 * subid 学科id
 * acid 行政班id
 * tcid 教学班id
 * sc 分数
 * crk 班级排名
 * crt 班级百分比 = 班级排名 / 班级总人数
 * grk 年级排名
 * grt 年级百分比 = 年级排名 / 年级总人数
 * cnm 班级名称
 */
public class ThreePlusThreeScoreEntry extends BaseDBObject {

    public ThreePlusThreeScoreEntry() {
    }

    public ThreePlusThreeScoreEntry(BasicDBObject baseEntry) {
        setBaseEntry(baseEntry);
    }

    public ThreePlusThreeScoreEntry(ObjectId examId, ObjectId studentId, ObjectId subjectId, ObjectId adminClassId, ObjectId teachingClassId,
                                    double score, int classRank, double classRate, double gradeRank, double gradeRate, String className) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("exid", examId)
                .append("stuid", studentId)
                .append("subid", subjectId)
                .append("acid", adminClassId)
                .append("tcid", teachingClassId)
                .append("sc", score)
                .append("crk", classRank)
                .append("crt", classRate)
                .append("grk", gradeRank)
                .append("grt", gradeRate)
                .append("cnm", className)
                ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getAdminClassId() {
        return getSimpleObjecIDValue("acid");
    }

    public void setAdminClassId(ObjectId adminClassId) {
        setSimpleValue("acid", adminClassId);
    }

    public int getClassRank() {
        return getSimpleIntegerValue("crk");
    }

    public void setClassRank(int classRank) {
        setSimpleValue("crk", classRank);
    }

    public double getClassRate() {
        return getSimpleDoubleValue("crt");
    }

    public void setClassRate(double classRate) {
        setSimpleValue("crt", classRate);
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exid");
    }

    public void setExamId(ObjectId examId) {
        setSimpleValue("exid", examId);
    }

    public double getGradeRate() {
        return getSimpleDoubleValue("grt");
    }

    public void setGradeRate(double gradeRate) {
        setSimpleValue("grt", gradeRate);
    }

    public int getGradeRank() {
        return getSimpleIntegerValue("grk");
    }

    public void setGradeRank(int gradeRank) {
        setSimpleValue("grk", gradeRank);
    }

    public double getScore() {
        return getSimpleDoubleValue("sc");
    }

    public void setScore(double score) {
       setSimpleValue("sc", score);
    }

    public ObjectId getStudentId() {
        return getSimpleObjecIDValue("stuid");
    }

    public void setStudentId(ObjectId studentId) {
        setSimpleValue("stuid", studentId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subid");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subid", subjectId);
    }

    public ObjectId getTeachingClassId() {
        return getSimpleObjecIDValue("tcid");
    }

    public void setTeachingClassId(ObjectId teachingClassId) {
        setSimpleValue("tcid", teachingClassId);
    }

    public String getClassName(){
        return getSimpleStringValue("cnm");
    }

    public void setClassName(String className){
        setSimpleValue("cnm", className);
    }
}
