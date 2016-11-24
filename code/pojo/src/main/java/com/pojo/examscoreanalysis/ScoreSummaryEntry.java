package com.pojo.examscoreanalysis;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**考试成绩的总结,中间数据
 * Created by fl on 2016/8/11.
 * exid 考试id
 * subid 科目id
 * subnm 科目名称
 * clsid 班级id  行政班id或教学班id
 * clsnm 班级名称
 * grdid 年级id
 * max 最高分
 * min 最低分
 * avg 平均分
 * count 人数
 * yxc 优秀人数
 * yxr 优秀率
 * hgc 合格人数
 * hgr 合格率
 * dfc 低分人数
 * dfr 低分率
 * rank 在年级排名
 * ty 类型  1表示班级层面  2表示年级层面
 */
public class ScoreSummaryEntry extends BaseDBObject {


    public ScoreSummaryEntry() {
    }

    public ScoreSummaryEntry(BasicDBObject baseEntry) {
        setBaseEntry(baseEntry);
    }

    public ScoreSummaryEntry(ObjectId examId, ObjectId subjectId, String subjectName, ObjectId classId, String className, ObjectId gradeId, double max, double min, double avg,
                             int count, int heGeCount, double heGeRate, int youXiuCount, double youXiuRate, int diFenCount, double diFenRate, int rank, int type) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("exid", examId)
                .append("subid", subjectId)
                .append("subnm", subjectName)
                .append("clsid", classId)
                .append("clsnm", className)
                .append("grdid", gradeId)
                .append("max", max)
                .append("min", min)
                .append("avg", avg)
                .append("count", count)
                .append("hgc", heGeCount)
                .append("hgr", heGeRate)
                .append("yxc", youXiuCount)
                .append("yxr", youXiuRate)
                .append("dfc", diFenCount)
                .append("dfr", diFenRate)
                .append("rank", rank)
                .append("ty", type)
                ;
        setBaseEntry(baseEntry);
    }

    public double getAvg() {
        return getSimpleDoubleValue("avg");
    }

    public void setAvg(double avg) {
        setSimpleValue("avg", avg);
    }

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("clsid");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("clsid", classId);
    }

    public int getCount() {
        return getSimpleIntegerValue("count");
    }

    public void setCount(int count) {
        setSimpleValue("count", count);
    }

    public ObjectId getExamId() {
        return getSimpleObjecIDValue("exid");
    }

    public void setExamId(ObjectId examId) {
        setSimpleValue("exid", examId);
    }

    public double getHeGeRate() {
        return getSimpleDoubleValue("hgr");
    }

    public void setHeGeRate(double heGeRate) {
        setSimpleValue("hgr", heGeRate);
    }

    public double getMax() {
        return getSimpleDoubleValue("max");
    }

    public void setMax(double max) {
        setSimpleValue("max", max);
    }

    public double getMin() {
        return getSimpleDoubleValue("min");
    }

    public void setMin(double min) {
        setSimpleValue("min", min);
    }

    public int getRank() {
        return getSimpleIntegerValue("rank");
    }

    public void setRank(int rank) {
        setSimpleValue("rank", rank);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subid");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subid", subjectId);
    }

    public double getYouXiuRate() {
        return getSimpleDoubleValue("yxr");
    }

    public void setYouXiuRate(double youXiuRate) {
        setSimpleValue("yxr", youXiuRate);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("grdid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("grdid", gradeId);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
    }

    public int getYouXiuCount() {
        return getSimpleIntegerValue("yxc");
    }

    public void setYouXiuCount(int youXiuCount) {
        setSimpleValue("yxc", youXiuCount);
    }

    public int getHeGeCount() {
        return getSimpleIntegerValue("hgc");
    }

    public void setHeGeCount(int heGeCount) {
        setSimpleValue("hgc", heGeCount);
    }

    public String getSubjectName(){
        return getSimpleStringValue("subnm");
    }

    public void setSubjectName(String subjectName){
        setSimpleValue("subnm", subjectName);
    }

    public String getClassName(){
        return getSimpleStringValue("clsnm");
    }

    public void setClassName(String className){
        setSimpleValue("clsnm", className);
    }

    public int getDiFenCount() {
        return getSimpleIntegerValueDef("dfc", 0);
    }

    public void setDiFenCount(int diFenCount) {
        setSimpleValue("dfc", diFenCount);
    }

    public double getDiFenRate() {
        return getSimpleDoubleValueDef("dfr", 0);
    }

    public void setDiFenRate(double diFenRate) {
        setSimpleValue("dfr", diFenRate);
    }
}
