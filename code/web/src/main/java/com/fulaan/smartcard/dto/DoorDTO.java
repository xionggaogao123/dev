package com.fulaan.smartcard.dto;

import com.pojo.smartcard.DoorInfoEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by guojing on 2016/6/23.
 */
public class DoorDTO {
    private String name;
    private String cardDate;
    private String doorState;
    private String doorNum;


    public DoorDTO(){

    }

    public DoorDTO(DoorInfoEntry e){
        this.name=e.getName();
        this.cardDate= DateTimeUtils.getLongToStrTimeTwo(e.getCardDate());
        this.doorState=e.getInOutFlag();
        this.doorNum=e.getDoorName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public String getDoorState() {
        return doorState;
    }

    public void setDoorState(String doorState) {
        this.doorState = doorState;
    }

    public String getDoorNum() {
        return doorNum;
    }

    public void setDoorNum(String doorNum) {
        this.doorNum = doorNum;
    }
}
