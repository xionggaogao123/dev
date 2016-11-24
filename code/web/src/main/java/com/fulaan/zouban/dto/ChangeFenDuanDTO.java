package com.fulaan.zouban.dto;

import com.fulaan.examresult.controller.IdNameDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/18.
 *
 * 调整分段DTO
 */
public class ChangeFenDuanDTO {
    private String classId;
    private String className;
    private String groupId;
    private List<IdNameDTO> groupList = new ArrayList<IdNameDTO>();

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<IdNameDTO> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<IdNameDTO> groupList) {
        this.groupList = groupList;
    }
}
