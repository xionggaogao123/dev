package com.fulaan.video.dto;

import java.util.List;

/**
 * Created by qinbo on 15/6/4.
 */
public class StudentVideoViewDTO {

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    private String videoName;
    private int viewNumber;
    private int endViewNumber;
    private int notViewNumber;
    private List<VideoViewRecordDTO> studentrecord;

    public StudentVideoViewDTO(){}
    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public int getEndViewNumber() {
        return endViewNumber;
    }

    public void setEndViewNumber(int endViewNumber) {
        this.endViewNumber = endViewNumber;
    }

    public int getNotViewNumber() {
        return notViewNumber;
    }

    public void setNotViewNumber(int notViewNumber) {
        this.notViewNumber = notViewNumber;
    }

    public List<VideoViewRecordDTO> getStudentrecord() {
        return studentrecord;
    }

    public void setStudentrecord(List<VideoViewRecordDTO> studentrecord) {
        this.studentrecord = studentrecord;
    }
}
