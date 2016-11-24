package com.fulaan.overallquality.dto;

import com.pojo.overallquality.OverallQualityGoodsEntry;

/**
 * Created by guojing on 2016/8/25.
 */
public class OverallQualityGoodsDTO {
    private String goodsId;
    private String goodsName;
    private String picPath;
    private int goodsPrice;
    private int goodsStock;
    private boolean isStu;
    private boolean isChange;

    public OverallQualityGoodsDTO(){

    }

    public OverallQualityGoodsDTO(OverallQualityGoodsEntry e){
        this.goodsId = e.getID().toString();
        this.goodsName = e.getGoodsName();
        this.picPath = e.getPicPath();
        this.goodsPrice = e.getGoodsPrice();
        this.goodsStock = e.getGoodsStock();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(int goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }

    public boolean getIsStu() {
        return isStu;
    }

    public void setIsStu(boolean isStu) {
        this.isStu = isStu;
    }

    public boolean getIsChange() {
        return isChange;
    }

    public void setIsChange(boolean isChange) {
        this.isChange = isChange;
    }
}
