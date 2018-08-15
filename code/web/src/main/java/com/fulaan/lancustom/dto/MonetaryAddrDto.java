package com.fulaan.lancustom.dto;

import com.pojo.lancustom.MonetaryAddrEntry;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 11:34
 * @Description:
 */
public class MonetaryAddrDto {
    //地址id
    private String id;
    //收货人
    private String name;
    //联系电话
    private String telphone;
    //所在地区
    private String area;
    //详细地址
    private String detail;
    //用户id
    private String userId;

    public MonetaryAddrDto() {

    }

    public MonetaryAddrDto(MonetaryAddrEntry entry) {
        this.id = entry.getID().toString();
        this.name = entry.getName();
        this.telphone = entry.getTelphone();
        this.area = entry.getArea();
        this.detail = entry.getDetail();
        this.userId = entry.getUserId().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
