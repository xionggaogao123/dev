package com.fulaan.dorm.dto;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.dormitory.LoopEntry;
import com.pojo.dormitory.RoomEntry;
import com.pojo.dormitory.RoomUserEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/9/27.
 */
public class RoomDTO {

    private int bedNum;

    private int occupancyNum;

    private String roomName;

    private String id;

    private String loopId;

    private String remark;

    private int roomType;

    private String loopName;

    private List<RoomUserDTO> userList;

    public RoomDTO() {

    }

    public RoomDTO(RoomEntry roomEntry,LoopEntry loopEntry) {
        List<RoomUserDTO> roomUserDTOs = new ArrayList<RoomUserDTO>();
        this.id = roomEntry.getID().toString();
        this.bedNum = roomEntry.getBedNum();
        this.occupancyNum = roomEntry.getOccupancyNum();
        this.roomName = roomEntry.getRoomName();
        this.loopId = roomEntry.getLoopId().toString();
        this.remark = roomEntry.getRemark();
        this.roomType = roomEntry.getRoomType();
        if (roomEntry.getSimpleDTOs()!=null && roomEntry.getSimpleDTOs().size()!=0) {
            for (RoomUserEntry roomUserEntry : roomEntry.getSimpleDTOs()) {
                roomUserDTOs.add(new RoomUserDTO(roomUserEntry));
            }
        }
        this.userList = roomUserDTOs;
        this.loopName = loopEntry.getLoopName();
    }

    public int getBedNum() {
        return bedNum;
    }

    public void setBedNum(int bedNum) {
        this.bedNum = bedNum;
    }

    public int getOccupancyNum() {
        return occupancyNum;
    }

    public void setOccupancyNum(int occupancyNum) {
        this.occupancyNum = occupancyNum;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoopId() {
        return loopId;
    }

    public void setLoopId(String loopId) {
        this.loopId = loopId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public List<RoomUserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<RoomUserDTO> userList) {
        this.userList = userList;
    }

    public String getLoopName() {
        return loopName;
    }

    public void setLoopName(String loopName) {
        this.loopName = loopName;
    }
}
