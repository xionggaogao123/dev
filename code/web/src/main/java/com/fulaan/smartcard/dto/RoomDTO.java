package com.fulaan.smartcard.dto;

/**
 * Created by wang_xinxin on 2016/9/7.
 */
public class RoomDTO {
    private String roomName;

    private int count;

    public RoomDTO() {

    }

    public RoomDTO(String name,int count) {
        this.roomName = name;
        this.count = count;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
