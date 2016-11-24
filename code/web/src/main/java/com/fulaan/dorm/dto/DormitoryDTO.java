package com.fulaan.dorm.dto;

import com.pojo.dormitory.DormitoryEntry;
import com.pojo.dormitory.LoopEntry;
import com.pojo.dormitory.RoomEntry;

/**
 * Created by wang_xinxin on 2016/9/13.
 */
public class DormitoryDTO {

    private String id;

    private String name;

    private String remark;

    private int bedNum;

    private int roomType;

    public DormitoryDTO() {

    }

    public DormitoryDTO(DormitoryEntry dorm) {
        this.id = dorm.getID().toString();
        this.name = dorm.getDormitoryName();
        this.remark = dorm.getRemark();
    }

    public DormitoryDTO(LoopEntry loop) {
        this.id = loop.getID().toString();
        this.name = loop.getLoopName();
        this.remark = loop.getRemark();
    }

    public DormitoryDTO(RoomEntry room) {
        this.id = room.getID().toString();
        this.name = room.getRoomName();
        this.remark = room.getRemark();
        this.bedNum = room.getBedNum();
        this.roomType = room.getRoomType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBedNum() {
        return bedNum;
    }

    public void setBedNum(int bedNum) {
        this.bedNum = bedNum;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }
}
