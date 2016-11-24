package com.fulaan.duty.dto;

import com.pojo.duty.DutyProjectEntry;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/1.
 */
public class DutyProjectDTO {
    private String id;

    private int index;

    private String content;

    private String orgId;

    private String orgContent;

    private int type;

    private int count;

    private List<DutyProjectDTO> dutyProject;

    public DutyProjectDTO() {

    }

    public DutyProjectDTO(DutyProjectEntry dutyProjectEntry) {
        this.id = dutyProjectEntry.getID().toString();
        this.index = dutyProjectEntry.getIndex();
        this.content = dutyProjectEntry.getContent();

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgContent() {
        return orgContent;
    }

    public void setOrgContent(String orgContent) {
        this.orgContent = orgContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<DutyProjectDTO> getDutyProject() {
        return dutyProject;
    }

    public void setDutyProject(List<DutyProjectDTO> dutyProject) {
        this.dutyProject = dutyProject;
    }
}
