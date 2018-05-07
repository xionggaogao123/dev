package com.fulaan.integralmall.dto;

import com.pojo.integralmall.GoodsEntry;
import com.pojo.integralmall.OrderEntry;
/**
 * 
 * <简述>商品dto
 * <详细描述>
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class GoodsDto {
    
    //商品id
    private String id;
    //图片路径
    private String avatar;
    //标签，以顿号隔开
    private String label;
    //商品名称
    private String name;
    //所需积分
    private Integer cost;
        //兑换次数
    private Long times;
    //商品简介
    private String description;
    //所属订单id
    private String orderId;
    //订单时间
    private String orderTimeStr;
    //是否已经申述  0为申述 1已申述
    private int isState;
    
    public GoodsDto() {
       
    }
    
    
    public GoodsDto(GoodsEntry entry) {
        this.id = entry.getID().toString();
        this.avatar = entry.getAvatar();
        this.label = entry.getLabel();
        this.name = entry.getName();
        this.cost = entry.getCost();
        this.times = entry.getTimes();
        this.description = entry.getDescription();
    }
    
    public GoodsDto(GoodsEntry entry, OrderEntry oentry) {
        this.id = entry.getID().toString();
        this.avatar = entry.getAvatar();
        this.label = entry.getLabel();
        this.name = entry.getName();
        this.cost = entry.getCost();
        this.times = entry.getTimes();
        this.description = entry.getDescription();
        this.orderId = oentry.getID().toString();
        this.orderTimeStr = oentry.getOrderTimeStr();
        this.isState = oentry.getIsState();
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public int getIsState() {
        return isState;
    }

    public void setIsState(int isState) {
        this.isState = isState;
    }
    
    
}
