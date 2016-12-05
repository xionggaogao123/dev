package com.fulaan.train.dto;

import com.pojo.train.InstituteEntry;

/**
 * Created by admin on 2016/12/2.
 */
public class InstituteDTO {

    private String id;
    private String mainPicture;
    private String address;
    private String telephone;
    private String createTime;
    private String description;
    private String businessTime;
    private String server;

    public InstituteDTO(){

    }

    public InstituteDTO(InstituteEntry entry){
        this.id=entry.getID().toString();
        this.mainPicture=entry.getMainPic();
        this.address=entry.getAddress();
        this.telephone=entry.getTelephone();
        this.createTime=entry.getChuangLiShiJian();
        this.description=entry.getShangHuJianJie();
        this.businessTime=entry.getYingYeShiJian();
        this.server=entry.getTeSeFuWu();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
