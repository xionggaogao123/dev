package com.pojo.exam;

/**
 * Created by Caocui on 2015/8/3.
 */
public class ExamRoomDTO {
    private String id;
    private String examRoomNumber;
    private String examRoomName;
    private int examRoomSitNumber;
    private String examRoomPostscript;

    public ExamRoomDTO() {
    }

    public ExamRoomDTO(ExamRoomEntry entry) {
        this.id = entry.getID().toString();
        this.examRoomName = entry.getExamRoomName();
        this.examRoomSitNumber = entry.getExamRoomSitNumber();
        this.examRoomPostscript = entry.getExamRoomPostscript();
        this.examRoomNumber = entry.getExamRoomNumber();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamRoomNumber() {
        return examRoomNumber;
    }

    public void setExamRoomNumber(String examRoomNumber) {
        this.examRoomNumber = examRoomNumber;
    }

    public String getExamRoomName() {
        return examRoomName;
    }

    public void setExamRoomName(String examRoomName) {
        this.examRoomName = examRoomName;
    }

    public int getExamRoomSitNumber() {
        return examRoomSitNumber;
    }

    public void setExamRoomSitNumber(int examRoomSitNumber) {
        this.examRoomSitNumber = examRoomSitNumber;
    }

    public String getExamRoomPostscript() {
        return examRoomPostscript;
    }

    public void setExamRoomPostscript(String examRoomPostscript) {
        this.examRoomPostscript = examRoomPostscript;
    }
}
