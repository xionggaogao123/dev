package com.fulaan.indicator.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/11/10.
 */
public class IndicatorTreeSnapshotDTO {

    private String id;
    private String treeId;
    private String schoolId;
    private String name;
    private String createrId;
    private String createrName;
    private String createDate;
    private List<IndicatorDTO> indiList = new ArrayList<IndicatorDTO>();
    private String describe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<IndicatorDTO> getIndiList() {
        return indiList;
    }

    public void setIndiList(List<IndicatorDTO> indiList) {
        this.indiList = indiList;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
