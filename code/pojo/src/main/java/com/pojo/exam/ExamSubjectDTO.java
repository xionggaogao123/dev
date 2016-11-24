package com.pojo.exam;

import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Caocui on 2015/7/26.
 */
public class ExamSubjectDTO {
    private String id;
    private String subjectId;
    private String subjectName;
    private int fullMarks;
    private double failScore;
    private double youXiuScore;
    private double diFenScore;
    private String time;
    private String examDate;
    private String weekDay;
    private int openStatus;
    private String openBeginTime;
    private String openEndTime;
    private String examId;
    private boolean canEdit;

    public ExamSubjectDTO() {
    }

    public ExamSubjectDTO(ExamSubjectEntry subjectEntry) {
        this.id = subjectEntry.getID().toString();
        this.subjectId = subjectEntry.getSubjectId().toString();
        this.subjectName = subjectEntry.getSubjectName();
        this.fullMarks = subjectEntry.getFullMarks();
        this.time = subjectEntry.getTime();
        this.examDate = subjectEntry.getExamDate();
        this.weekDay = subjectEntry.getWeekDay();
        this.openStatus = subjectEntry.getOpenStatus();
        this.openBeginTime = subjectEntry.getOpenBeginTime();
        this.openEndTime = subjectEntry.getOpenEndTime();
        this.failScore = subjectEntry.getFailScore();
        this.youXiuScore = subjectEntry.getYouXiuScore();
        this.diFenScore = subjectEntry.getDiFenScore();
    }

    public String getId() {
        return id;

    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getFullMarks() {
        return fullMarks;
    }

    public void setFullMarks(int fullMarks) {
        this.fullMarks = fullMarks;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
        if (!StringUtils.isEmpty(examDate)) {
            Calendar cal = Calendar.getInstance(Locale.CHINA);

            cal.setTime(DateTimeUtils.stringToDate(examDate, DateTimeUtils.DATE_YYYY_MM_DD));
            this.weekDay = Constant.CHINESE_WEEK[cal.get(Calendar.DAY_OF_WEEK) - 1];
        }
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(int openStatus) {
        this.openStatus = openStatus;
    }

    public String getOpenBeginTime() {
        return openBeginTime;
    }

    public void setOpenBeginTime(String openBeginTime) {
        this.openBeginTime = openBeginTime;
    }

    public String getOpenEndTime() {
        return openEndTime;
    }

    public void setOpenEndTime(String openEndTime) {
        this.openEndTime = openEndTime;
    }

    public boolean isCanEdit() {
        if (this.openStatus == 0) {
            return false;
        } else {
            if (StringUtils.isNotEmpty(this.openBeginTime) && StringUtils.isNotEmpty(this.openEndTime)) {
                String curr = DateTimeUtils.convert(new Date().getTime(), DateTimeUtils.DATE_YYYY_MM_DD);
                if ((DateTimeUtils.compare_date2(curr, openBeginTime) > -1) && (DateTimeUtils.compare_date2(curr, openEndTime) < 1)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public double getFailScore() {
        return failScore;
    }

    public void setFailScore(double failScore) {
        this.failScore = failScore;
    }

    public double getYouXiuScore() {
        return youXiuScore;
    }

    public void setYouXiuScore(double youXiuScore) {
        this.youXiuScore = youXiuScore;
    }

    public double getDiFenScore() {
        return diFenScore;
    }

    public void setDiFenScore(double diFenScore) {
        this.diFenScore = diFenScore;
    }
}
