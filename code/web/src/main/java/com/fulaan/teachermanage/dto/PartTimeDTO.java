package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.PartTimeEntry;

/**
 * Created by wang_xinxin on 2016/3/8.
 */
public class PartTimeDTO {
    private String time;
    private String unit;
    private String position;
    private String open;
    public PartTimeDTO() {

    }
    public PartTimeDTO(PartTimeEntry entry) {
        this.time = entry.getTime();
        this.unit = entry.getUnit();
        this.position = entry.getPosition();
        this.open = String.valueOf(entry.getOpen());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}
