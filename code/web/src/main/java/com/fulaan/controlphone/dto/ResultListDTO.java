package com.fulaan.controlphone.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/9.
 */
public class ResultListDTO {

    private List<ControlMapDTO> mapList = new ArrayList<ControlMapDTO>();


    public List<ControlMapDTO> getMapList() {
        return mapList;
    }

    public void setMapList(List<ControlMapDTO> mapList) {
        this.mapList = mapList;
    }
}
