package com.fulaan.indicator.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/10/17.
 */
public class IndicatorDTO {
    private String id;
    private String zid;
    private String treeId;
    private int version;
    private String schoolId;
    private int type;
    private String teacherId;
    private String name;
    private String parentId;
    private int sort;
    private List<String> parentIds=new ArrayList<String>();
    private String parentIdsStr;
    private int level;
    private double weight;
    private String termValidity;
    private List<IndicatorLog> logs=new ArrayList<IndicatorLog>();
    private int isValid;
    private String userId;
    private boolean isHandle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getParentIdsStr() {
        return parentIdsStr;
    }

    public void setParentIdsStr(String parentIdsStr) {
        this.parentIdsStr = parentIdsStr;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getTermValidity() {
        return termValidity;
    }

    public void setTermValidity(String termValidity) {
        this.termValidity = termValidity;
    }

    public List<IndicatorLog> getLogs() {
        return logs;
    }

    public void setLogs(List<IndicatorLog> logs) {
        this.logs = logs;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(boolean isHandle) {
        this.isHandle = isHandle;
    }
}
