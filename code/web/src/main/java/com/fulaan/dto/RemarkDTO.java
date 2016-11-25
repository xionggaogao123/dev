package com.fulaan.dto;


import com.pojo.fcommunity.RemarkEntry;

/**
 * Created by admin on 2016/11/1.
 */
public class RemarkDTO {
    private String id;
    private String remark;

    public RemarkDTO(RemarkEntry entry) {
        this.id = entry.getID().toString();
        this.remark = entry.getRemark();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
