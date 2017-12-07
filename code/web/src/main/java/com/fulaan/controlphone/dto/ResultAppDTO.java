package com.fulaan.controlphone.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/9.
 */
public class ResultAppDTO {

    private List<ControlAppResultDTO> appList = new ArrayList<ControlAppResultDTO>();
    private long addiction;


    public List<ControlAppResultDTO> getAppList() {
        return appList;
    }

    public void setAppList(List<ControlAppResultDTO> appList) {
        this.appList = appList;
    }

    public long getAddiction() {
        return addiction;
    }

    public void setAddiction(long addiction) {
        this.addiction = addiction;
    }
}
