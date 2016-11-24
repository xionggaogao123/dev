package com.fulaan.zouban.dto;

import com.pojo.app.IdNameValuePairDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/16.
 */
public class FenDuanDTO {
    private String id;
    private int group;
    private List<IdNameValuePairDTO> classList = new ArrayList<IdNameValuePairDTO>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getGroupName() {
        return "第" + group + "段";
    }

    public int getCount() {
        return classList.size();
    }

    public List<IdNameValuePairDTO> getClassList() {
        return classList;
    }

    public void setClassList(List<IdNameValuePairDTO> classList) {
        this.classList = classList;
    }
}
