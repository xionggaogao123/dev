package com.fulaan.lancustom.dto;

import com.pojo.lancustom.MonetaryGoodsEntry;
import com.pojo.lancustom.PhoneCostEntry;

/**
 * @Auther: taotao.chan
 * @Date: 2018/11/16 13:18
 * @Description: 手机维修价目表
 */
public class PhoneCostDto {
    private String id;
    private String model;
    private String costPicUrl;

    public PhoneCostDto(MonetaryGoodsEntry entry){
        if (entry != null){
            this.id = entry.getID().toString();
            this.model = entry.getName();
            this.costPicUrl = entry.getRepairCostPic();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCostPicUrl() {
        return costPicUrl;
    }

    public void setCostPicUrl(String costPicUrl) {
        this.costPicUrl = costPicUrl;
    }
}

