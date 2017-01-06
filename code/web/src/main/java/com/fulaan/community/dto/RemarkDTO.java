package com.fulaan.community.dto;


import com.pojo.fcommunity.RemarkEntry;

/**
 * Created by admin on 2017/1/6.
 */
public class RemarkDTO {
    private String id;
    private String startUserId;
    private String endUserId;
    private String remark;

    public RemarkDTO(RemarkEntry entry){
        this.id=entry.getID().toString();
        this.startUserId=entry.getStartUserId().toString();
        this.endUserId=entry.getEndUserId().toString();
        this.remark=entry.getRemark();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
