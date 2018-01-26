package com.fulaan.controlphone.dto;

/**
 * Created by James on 2018/1/24.
 */
public class ControlMiduleDTO {

    private int isControl;
    private ControlSchoolTimeDTO dto;
    private long time;
    private ControlNowTimeDTO third;

    public int getIsControl() {
        return isControl;
    }

    public void setIsControl(int isControl) {
        this.isControl = isControl;
    }

    public ControlSchoolTimeDTO getDto() {
        return dto;
    }

    public void setDto(ControlSchoolTimeDTO dto) {
        this.dto = dto;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ControlNowTimeDTO getThird() {
        return third;
    }

    public void setThird(ControlNowTimeDTO third) {
        this.third = third;
    }
}
