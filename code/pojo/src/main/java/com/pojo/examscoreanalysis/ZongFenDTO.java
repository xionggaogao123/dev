package com.pojo.examscoreanalysis;

/**
 * Created by fl on 2016/8/23.
 */
public class ZongFenDTO {

    private String id;

    private String studentName;

    private String examId;

    private String studentId;

    private double yswZongFen;

    private int yswClassRank;

    private int yswGradeRank;

    private double zongFen;

    private int classRank;

    private int gradeRank;

    private String className;

    private String studentNo;


    public ZongFenDTO() {
    }

    public ZongFenDTO(String id, String examId, String studentId, String studentName, double yswZongFen, int yswClassRank, int yswGradeRank,
                      double zongFen, int classRank, int gradeRank, String className, String studentNo) {
        this.classRank = classRank;
        this.examId = examId;
        this.gradeRank = gradeRank;
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.yswClassRank = yswClassRank;
        this.yswGradeRank = yswGradeRank;
        this.yswZongFen = yswZongFen;
        this.zongFen = zongFen;
        this.className = className;
        this.studentNo = studentNo;
    }

    public ZongFenDTO(ZongFenEntry entry) {
        this.classRank = entry.getClassRank();
        this.examId = entry.getExamId().toString();
        this.gradeRank = entry.getGradeRank();
        this.id = entry.getID().toString();
        this.studentId = entry.getStudentId().toString();
        this.yswClassRank = entry.getYswClassRank();
        this.yswGradeRank = entry.getYswGradeRank();
        this.yswZongFen = entry.getYswZongFen();
        this.zongFen = entry.getZongFen();
    }

    public int getClassRank() {
        return classRank;
    }

    public void setClassRank(int classRank) {
        this.classRank = classRank;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getYswClassRank() {
        return yswClassRank;
    }

    public void setYswClassRank(int yswClassRank) {
        this.yswClassRank = yswClassRank;
    }

    public int getYswGradeRank() {
        return yswGradeRank;
    }

    public void setYswGradeRank(int yswGradeRank) {
        this.yswGradeRank = yswGradeRank;
    }

    public double getYswZongFen() {
        return yswZongFen;
    }

    public void setYswZongFen(double yswZongFen) {
        this.yswZongFen = yswZongFen;
    }

    public double getZongFen() {
        return zongFen;
    }

    public void setZongFen(double zongFen) {
        this.zongFen = zongFen;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
}
