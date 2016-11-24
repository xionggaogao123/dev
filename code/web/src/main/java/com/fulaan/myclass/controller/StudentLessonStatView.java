package com.fulaan.myclass.controller;

import org.bson.types.ObjectId;

/**
 * Created by Hao on 2015/3/31.
 */
public class StudentLessonStatView {
    private ObjectId documentId;
    private String lessonName;
    private boolean isView;
    private int doneExerciseCount;
    private int totalExerciseCount;
    private String correctRate;



    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public boolean getIsView() {
        return isView;
    }

    public void setIsView(boolean isView) {
        this.isView = isView;
    }

    public int getDoneExerciseCount() {
        return doneExerciseCount;
    }

    public void setDoneExerciseCount(int doneExerciseCount) {
        this.doneExerciseCount = doneExerciseCount;
    }

    public int getTotalExerciseCount() {
        return totalExerciseCount;
    }

    public void setTotalExerciseCount(int totalExerciseCount) {
        this.totalExerciseCount = totalExerciseCount;
    }

    public ObjectId getDocumentId() {
        return documentId;
    }

    public void setDocumentId(ObjectId documentId) {
        this.documentId = documentId;
    }

    public String getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(String correctRate) {
        this.correctRate = correctRate;
    }
}
