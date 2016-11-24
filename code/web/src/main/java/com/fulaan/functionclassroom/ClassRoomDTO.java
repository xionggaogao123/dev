package com.fulaan.functionclassroom;

/**
 * Created by wang_xinxin on 2016/10/13.
 */
public class ClassRoomDTO {

    private String classRoomId;

    private int number;

    private String classRoomName;

    private String[] users;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(String classRoomId) {
        this.classRoomId = classRoomId;
    }
}
