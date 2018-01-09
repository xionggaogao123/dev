package com.fulaan.smalllesson.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/1/8.
 */
public class ResultTypeDTO {
    private List<String> userIds = new ArrayList<String>();
    private String lessonId;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }
}
