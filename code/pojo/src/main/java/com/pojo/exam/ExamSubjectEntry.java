package com.pojo.exam;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by Caocui on 2015/7/22.
 * 考试科目实体对象，依附于ExamEntry对象
 * 对应数据库集合 Constant.COLLECTION_EXAMSUBJECT = "examSubject"
 * 科目编码:sid
 * 科目名称:sna
 * 满分:fm
 * 及格分:fl
 * 考试日期:ed
 * 考试时间对应周几:wd
 * 考试时间段:tm
 * 成绩录入开放状态:os
 * 成绩录入开始时间:obt
 * 成绩录入结束时间:oet
 */
public class ExamSubjectEntry extends BaseDBObject {
    public ExamSubjectEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ExamSubjectEntry(ObjectId subjectId, String subjectName, int fullMarks,double failScore, String time, String examDate, String weekDay, int openStatus,
                            String openBeginTime, String openEndTime) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("sid", subjectId)
                .append("sna", subjectName)
                .append("fm", fullMarks)
                .append("fl",failScore)
                .append("tm", time)
                .append("ed", examDate)
                .append("wd", weekDay)
                .append("os", openStatus)
                .append("obt", openBeginTime)
                .append("oet", openEndTime)
                .append(Constant.ID, new ObjectId());

        setBaseEntry(baseEntry);
    }

    public double getFailScore() {
        return getSimpleDoubleValue("fl");
    }

    public void setFailScore(double failScore) {
        setSimpleValue("fl", failScore);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("sid", subjectId);
    }

    public String getSubjectName() {
        return getSimpleStringValue("sna");
    }

    public void setSubjectName(String subjectName) {
        setSimpleValue("sna", subjectName);
    }

    public int getFullMarks() {
        return getSimpleIntegerValue("fm");
    }

    public void setFullMarks(int fullMarks) {
        setSimpleValue("fm", fullMarks);
    }

    public String getTime() {
        return getSimpleStringValue("tm");
    }

    public void setTime(String time) {
        setSimpleValue("tm", time);
    }

    public String getExamDate() {
        return getSimpleStringValue("ed");
    }

    public void setExamDate(String examDate) {
        setSimpleValue("ed", examDate);
    }

    public int getOpenStatus() {
        return getSimpleIntegerValue("os");
    }

    public void setOpenStatus(int status) {
        setSimpleValue("os", status);
    }

    public String getOpenBeginTime() {
        return getSimpleStringValue("obt");
    }

    public void setOpenBeginTime(String openBeginTime) {
        setSimpleValue("obt", openBeginTime);
    }

    public String getOpenEndTime() {
        return getSimpleStringValue("oet");
    }

    public void setOpenEndTime(String openEndTime) {
        setSimpleValue("oet", openEndTime);
    }

    public String getWeekDay() {
        return getSimpleStringValue("wd");
    }

    public void setWeekDay(String weekDay) {
        setSimpleValue("wd", weekDay);
    }
}
