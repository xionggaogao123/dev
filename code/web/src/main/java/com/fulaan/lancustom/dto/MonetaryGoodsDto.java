package com.fulaan.lancustom.dto;

import com.pojo.lancustom.MonetaryGoodsEntry;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/6 15:43
 * @Description: 商品实体
 */
public class MonetaryGoodsDto {

    //商品id
    private String id;
    //列表首页图
    private String avatar;
    //商品详情图片，多张图片路径用 中文顿号 隔开的
    private String pic;
    //标签，以顿号隔开
    private String label;
    //款式，以顿号隔开
    private String style;
    //商品名称
    private String name;
    //价格
    private Double money;
    //商品简介
    private String description;

    //维修价目表
    private String repairCostPic;

    public MonetaryGoodsDto(){

    }

    public MonetaryGoodsDto(MonetaryGoodsEntry entry) {
        this.id = entry.getID().toString();
        this.avatar = entry.getAvatar();
        this.pic = entry.getPic();
        this.label = entry.getLabel();
        this.style = entry.getStyle();
        this.name = entry.getName();
        this.money = entry.getMoney();
        this.description = entry.getDescription();
        this.repairCostPic = entry.getRepairCostPic();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MonetaryGoodsEntry buildAddEntry() {
        MonetaryGoodsEntry entry = new MonetaryGoodsEntry(this.avatar, this.pic, this.label, this.style, this.name, this.money, this.description, this.repairCostPic);
        return entry;
    }

    public String getRepairCostPic() {
        return repairCostPic;
    }

    public void setRepairCostPic(String repairCostPic) {
        this.repairCostPic = repairCostPic;
    }
}
