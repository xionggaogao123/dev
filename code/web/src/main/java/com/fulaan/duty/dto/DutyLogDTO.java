package com.fulaan.duty.dto;

import com.fulaan.overtime.dto.OverTimeDTO;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/9/21.
 */
public class DutyLogDTO {

    private String date;

    private List<DutyUserDTO> dutyUserDTOList;

    private List<OverTimeDTO> overTimeDTOList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DutyUserDTO> getDutyUserDTOList() {
        return dutyUserDTOList;
    }

    public void setDutyUserDTOList(List<DutyUserDTO> dutyUserDTOList) {
        this.dutyUserDTOList = dutyUserDTOList;
    }

    public List<OverTimeDTO> getOverTimeDTOList() {
        return overTimeDTOList;
    }

    public void setOverTimeDTOList(List<OverTimeDTO> overTimeDTOList) {
        this.overTimeDTOList = overTimeDTOList;
    }
}
