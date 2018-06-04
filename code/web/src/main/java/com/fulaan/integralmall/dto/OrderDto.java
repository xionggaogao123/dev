package com.fulaan.integralmall.dto;

import com.fulaan.dto.UserDTO;
import com.pojo.integralmall.AddressEntry;
import com.pojo.integralmall.GoodsEntry;
import com.pojo.integralmall.OrderEntry;
import com.pojo.user.UserEntry;
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
    //订单号
    private String orderNo;
    //物流公司
    private String excompanyNo;
    //订单编号
    private String expressNo;
    //是否已经申述 0否 1是
    private Integer isState;
    //申述内容
    private String stateReason;
    //状态 0未受理 1已受理
    private Integer state;
    //
    private String stateStr;
    //商品
    private GoodsDto goodsDto;
    //地址
    private AddressDto addressDto;
    //购买人
    private UserDTO userDto;
    //您的积分
    private Integer score;
    
    private String orderTimeStr;
    
    
    public OrderDto() {

    }
    
    public OrderDto(GoodsEntry goodsEntry, AddressEntry addressEntry,OrderEntry orderEntry, UserEntry userEntry) {
        this.id = orderEntry.getID().toString();
        this.orderNo = orderEntry.getOrderNum();
        this.goodNum = orderEntry.getGoodNum();
        this.excompanyNo = orderEntry.getExcompanyNo();
        this.expressNo = orderEntry.getExpressNo();
        this.isState = orderEntry.getIsState();
        this.stateReason = orderEntry.getStateReason();
        this.goodId = goodsEntry.getID().toString();
        this.goodsDto = new GoodsDto(goodsEntry);
        this.addressDto = new AddressDto(addressEntry);
        this.userDto = new UserDTO(userEntry);
        this.state = orderEntry.getState();
        this.orderTimeStr = orderEntry.getOrderTimeStr();
        if (orderEntry.getState() == 0) {
            this.stateStr = "未受理";
        } else {
            this.stateStr = "已受理";
        }
    }
    
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
    
    
    
    

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public UserDTO getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDTO userDto) {
        this.userDto = userDto;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }
    
    
}
