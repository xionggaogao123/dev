package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.PostionEntry;

/**
 * Created by wang_xinxin on 2016/3/8.
 */
public class PostionDTO {
    private String jobtime;
    private String organization;
    private String position;
    private String open;
    public PostionDTO() {

    }
    public PostionDTO(PostionEntry entry) {
        this.jobtime = entry.getJobtime();
        this.organization = entry.getOrganization();
        this.position = entry.getPosition();
        this.open = String.valueOf(entry.getOpen());
    }
    public String getJobtime() {
        return jobtime;
    }

    public void setJobtime(String jobtime) {
        this.jobtime = jobtime;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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
