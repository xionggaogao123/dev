package com.pojo.teacherevaluation;

import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePairDTO1;

import java.util.List;

/**
 * Created by fl on 2016/9/13.
 */
public class EvaluationItemDTO {

    private String id;

    private String evaluationId;

    private String teacherId;

    private String teacherName;

    private List<IdNameValuePairDTO> elementScores;

    private double finalScore;

    private int rank;

    public EvaluationItemDTO() {
    }

    public EvaluationItemDTO(EvaluationItemEntry entry) {
        this.id = entry.getID().toString();
        this.evaluationId = entry.getEvaluationId().toString();
        this.teacherId = entry.getTeacherId().toString();
    }

    public EvaluationItemDTO(String id, String evaluationId, String teacherId, String teacherName, List<IdNameValuePairDTO> elementScores, double finalScore, int rank) {
        this.elementScores = elementScores;
        this.evaluationId = evaluationId;
        this.finalScore = finalScore;
        this.id = id;
        this.rank = rank;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    public List<IdNameValuePairDTO> getElementScores() {
        return elementScores;
    }

    public void setElementScores(List<IdNameValuePairDTO> elementScores) {
        this.elementScores = elementScores;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
