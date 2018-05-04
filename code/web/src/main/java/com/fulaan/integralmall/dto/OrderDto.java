package com.fulaan.integralmall.dto;

import com.pojo.integralmall.AddressEntry;
import com.pojo.integralmall.GoodsEntry;
import com.pojo.integralmall.OrderEntry;
/**
 * 
 * <简述>订单dto
 * <详细描述>
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class OrderDto {

    //订单id
    private String id;
    //用户id
    private String userId;
    //商品id
    private String goodId;
    //商品数量
    private Integer goodNum;
    //物流公司
    private String excompanyNo;
    //订单编号
    private String expressNo;
    //是否已经申述 0否 1是
    private Integer isState;
    //申述内容
    private String stateReason;
    //商品
    private GoodsDto goodsDto;
    //地址
    private AddressDto addressDto;
    //您的积分
    private Integer score;
    
    public OrderDto(GoodsEntry goodsEntry, AddressEntry addressEntry,int score) {
        this.goodId = goodsEntry.getID().toString();
        this.goodsDto = new GoodsDto(goodsEntry);
        this.addressDto = new AddressDto(addressEntry);
        this.score = score;
    }
    
    public OrderDto(GoodsEntry goodsEntry, int score) {
        this.goodId = goodsEntry.getID().toString();
        this.goodsDto = new GoodsDto(goodsEntry);
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

    public String getExcompanyNo() {
        return excompanyNo;
    }

    public void setExcompanyNo(String excompanyNo) {
        this.excompanyNo = excompanyNo;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public Integer getIsState() {
        return isState;
    }

    public void setIsState(Integer isState) {
        this.isState = isState;
    }

    public String getStateReason() {
        return stateReason;
    }

    public void setStateReason(String stateReason) {
        this.stateReason = stateReason;
    }

    public GoodsDto getGoodsDto() {
        return goodsDto;
    }

    public void setGoodsDto(GoodsDto goodsDto) {
        this.goodsDto = goodsDto;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
    
    
}
