package com.pojo.exam;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Caocui on 2015/7/30.
 */
public class SubjectScoreDTO {
    private String id;
    private String subjectId;
    private String score;
    private int showType;
    NumberFormat format = new DecimalFormat("###.###");
    public SubjectScoreDTO() {

    }

    public SubjectScoreDTO(SubjectScoreEntry entry) {
//        this.id = entry.getID().toString();
        this.subjectId = entry.getSubjectId().toString();
        this.score = format.format(entry.getScore());
        this.showType = entry.getShowType();
    }

    public String getId() {
        return id;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }
}
