package com.pojo.examscoreanalysis;

import com.pojo.examscoreanalysis.ScoreSummaryEntry;

/**
 * Created by fl on 2016/8/12.
 */
public class ScoreSummaryDTO {

    private String id;

    private String examId;

    private String subjectId;

    private String subjectName;

    private String classId;

    private String className;

    private String gradeId;

    private double max;

    private double min;

    private double avg;

    private int count;

    private int heGeCount;

    private double heGeRate;

    private int youXiuCount;

    private double youXiuRate;

    private int diFenCount;

    private double diFenRate;

    private int rank;

    private int type;


    public ScoreSummaryDTO() {
    }

    public ScoreSummaryDTO(double avg, String classId, String className, int count, String examId, String gradeId, int heGeCount, double heGeRate,
                           String id, double max, double min, int rank, String subjectId, String subjectName, int type, int youXiuCount, double youXiuRate,
                           int diFenCount, double diFenRate) {
        this.avg = avg;
        this.classId = classId;
        this.className = className;
        this.count = count;
        this.examId = examId;
        this.gradeId = gradeId;
        this.heGeCount = heGeCount;
        this.heGeRate = heGeRate;
        this.id = id;
        this.max = max;
        this.min = min;
        this.rank = rank;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.type = type;
        this.youXiuCount = youXiuCount;
        this.youXiuRate = youXiuRate;
        this.diFenCount = diFenCount;
        this.diFenRate = diFenRate;
    }

    public ScoreSummaryDTO(ScoreSummaryEntry entry){
        this.id = entry.getID() == null ? null : entry.getID().toString();
        this.avg = entry.getAvg();
        this.classId = entry.getClassId() == null ? "" : entry.getClassId().toString();
        this.className = entry.getClassName() == null ? "" : entry.getClassName();
        this.count = entry.getCount();
        this.examId = entry.getExamId().toString();
        this.gradeId = entry.getGradeId().toString();
        this.heGeCount = entry.getHeGeCount();
        this.heGeRate = entry.getHeGeRate();
        this.max = entry.getMax();
        this.min = entry.getMin();
        this.rank = entry.getRank();
        this.subjectId = entry.getSubjectId().toString();
        this.subjectName = entry.getSubjectName();
        this.type = entry.getType();
        this.youXiuCount = entry.getYouXiuCount();
        this.youXiuRate = entry.getYouXiuRate();
        this.diFenCount = entry.getDiFenCount();
        this.diFenRate = entry.getDiFenRate();
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public double getHeGeRate() {
        return heGeRate;
    }

    public void setHeGeRate(double heGeRate) {
        this.heGeRate = heGeRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getYouXiuRate() {
        return youXiuRate;
    }

    public void setYouXiuRate(double youXiuRate) {
        this.youXiuRate = youXiuRate;
    }

    public int getHeGeCount() {
        return heGeCount;
    }

    public void setHeGeCount(int heGeCount) {
        this.heGeCount = heGeCount;
    }

    public int getYouXiuCount() {
        return youXiuCount;
    }

    public void setYouXiuCount(int youXiuCount) {
        this.youXiuCount = youXiuCount;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getDiFenCount() {
        return diFenCount;
    }

    public void setDiFenCount(int diFenCount) {
        this.diFenCount = diFenCount;
    }

    public double getDiFenRate() {
        return diFenRate;
    }

    public void setDiFenRate(double diFenRate) {
        this.diFenRate = diFenRate;
    }
}
