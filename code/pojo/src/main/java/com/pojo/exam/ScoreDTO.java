package com.pojo.exam;

import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caocui on 2015/7/22.
 * 考试成绩信息视图对象
 */
public class ScoreDTO {
    public static final int SCORE_TYPE_NORMAL = 0;
    public static final int SCORE_TYPE_ABSENT = 1;
    public static final int SCORE_TYPE_EXEMPTION = 2;
    private String id;
    private String examId;
    private String studentId;
    private String studentName;
    private String classId;
    private String className;
    private String examRoomId;
    private String examRoomNumber;
    private String examRoomName;
    private String examNumber;
    private String sum;
    private Map<String, SubjectScoreDTO> examScore;
    private List<Map<String, Object>> zoubanSubject = new ArrayList<Map<String, Object>>();
    NumberFormat format = new DecimalFormat("###.###");

    public ScoreDTO() {
    }

    public ScoreDTO(ScoreEntry entry) {
        this.id = entry.getID().toString();
        this.examId = entry.getExamId().toString();
        this.studentId = entry.getStudentId().toString();
        this.studentName = entry.getStudentName();
        this.classId = entry.getClassId().toString();
        this.className = entry.getClassName();
        this.examRoomId = entry.getExamRoomId() == null ? "" : entry.getExamRoomId().toString();
        this.examRoomNumber = entry.getExamRoomNumber() == null ? "" : entry.getExamRoomNumber();
        this.examRoomName = entry.getExamRoomName() == null ? "" : entry.getExamRoomName();
        this.examNumber = entry.getExamNum() == null ? "" : entry.getExamNum();
        this.sum = format.format(entry.getScoreSum());
        this.examScore = new HashMap<String, SubjectScoreDTO>(entry.getExamScore().size());
        for (SubjectScoreEntry score : entry.getExamScore()) {
            examScore.put(score.getSubjectId().toString(), new SubjectScoreDTO(score));
        }
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public String getExamRoomNumber() {
        return examRoomNumber;
    }

    public void setExamRoomNumber(String examRoomNumber) {
        this.examRoomNumber = examRoomNumber;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public Map<String, SubjectScoreDTO> getExamScore() {
        return examScore;
    }

    public void setExamScore(Map<String, SubjectScoreDTO> examScore) {
        this.examScore = examScore;
    }

    public String getExamId() {
        return examId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExamRoomId() {
        return examRoomId;
    }

    public void setExamRoomId(String examRoomId) {
        this.examRoomId = examRoomId;
    }

    public String getExamRoomName() {
        return examRoomName;
    }

    public void setExamRoomName(String examRoomName) {
        this.examRoomName = examRoomName;
    }

    public String getSubjectScore(String subject) {
        if (StringUtils.isEmpty(subject) || examScore == null || examScore.isEmpty()) {
            return Constant.EMPTY;
        }
        SubjectScoreDTO data = examScore.get(subject);
        return data == null ?
                Constant.EMPTY : data.getShowType() == SCORE_TYPE_ABSENT ?
                "缺" : data.getShowType() == SCORE_TYPE_EXEMPTION ?
                "免" : data.getScore();
    }

    public static double getSumScore(List<SubjectScoreEntry> scores) {
        double sum = 0f;
        for (SubjectScoreEntry val : scores) {
            //过滤缺考，免考数据
            sum += (val.getShowType() == 0 ? val.getScore() : 0);
        }
        return sum;
    }

    public List<Map<String, Object>> getZoubanSubject() {
        return zoubanSubject;
    }

    public void setZoubanSubject(List<Map<String, Object>> zoubanSubject) {
        this.zoubanSubject = zoubanSubject;
    }
}
