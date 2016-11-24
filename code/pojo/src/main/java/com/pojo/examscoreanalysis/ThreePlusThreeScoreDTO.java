package com.pojo.examscoreanalysis;

/**
 * Created by fl on 2016/8/12.
 */
public class ThreePlusThreeScoreDTO {

    private String id;

    private String examId;

    private String studentId;

    private String subjectId;

    private String adminClassId;

    private String teachingClassId;

    private double score;

    private int classRank;

    private double classRate;

    private int gradeRank;

    private double gradeRate;

    private String studentName;

    private String studentNo;

    private String className;

    public ThreePlusThreeScoreDTO() {
    }

    public ThreePlusThreeScoreDTO(String adminClassId, int classRank, double classRate, String examId, int gradeRank,
                                  double gradeRate, String id, double score, String studentId, String subjectId, String teachingClassId,
                                  String studentName, String studentNo, String className) {
        this.adminClassId = adminClassId;
        this.classRank = classRank;
        this.classRate = classRate;
        this.examId = examId;
        this.gradeRank = gradeRank;
        this.gradeRate = gradeRate;
        this.id = id;
        this.score = score;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.teachingClassId = teachingClassId;
        this.studentName = studentName;
        this.studentNo = studentNo;
        this.className = className;
    }

    public ThreePlusThreeScoreDTO(ThreePlusThreeScoreEntry entry){
        this.adminClassId = entry.getAdminClassId().toString();
        this.classRank = entry.getClassRank();
        this.classRate = entry.getClassRate();
        this.examId = entry.getExamId().toString();
        this.gradeRank = entry.getGradeRank();
        this.gradeRate = entry.getGradeRate();
        this.id = entry.getID() == null ? null : entry.getID().toString();
        this.score = entry.getScore();
        this.studentId = entry.getStudentId().toString();
        this.subjectId = entry.getSubjectId().toString();
        this.teachingClassId = entry.getTeachingClassId() == null ? "" : entry.getTeachingClassId().toString();
        this.className = entry.getClassName();
    }

    public String getAdminClassId() {
        return adminClassId;
    }

    public void setAdminClassId(String adminClassId) {
        this.adminClassId = adminClassId;
    }

    public int getClassRank() {
        return classRank;
    }

    public void setClassRank(int classRank) {
        this.classRank = classRank;
    }

    public double getClassRate() {
        return classRate;
    }

    public void setClassRate(double classRate) {
        this.classRate = classRate;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public int getGradeRank() {
        return gradeRank;
    }

    public void setGradeRank(int gradeRank) {
        this.gradeRank = gradeRank;
    }

    public double getGradeRate() {
        return gradeRate;
    }

    public void setGradeRate(double gradeRate) {
        this.gradeRate = gradeRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(String teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
