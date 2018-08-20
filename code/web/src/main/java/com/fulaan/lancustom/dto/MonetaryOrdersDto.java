package com.fulaan.lancustom.dto;

import com.fulaan.dto.UserDTO;
import com.fulaan.integralmall.dto.GoodsDto;
import com.pojo.lancustom.MonetaryAddrEntry;
import com.pojo.lancustom.MonetaryGoodsEntry;
import com.pojo.lancustom.MonetaryOrdersEntry;
import com.pojo.user.UserEntry;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 16:59
 * @Description:
 */
public class MonetaryOrdersDto {

    private String orderId;
    private String userId;
    private String goodId;
    private String addressId;
    private int goodNum;
    private String style;
    private Double money;
    //订单编号
    private String orderNo;
    //物快递公司编号
    private String excompanyNo;
    //快递编号
    private String expressNo;
    private String orderTimeStr;
    //订单状态 0 未支付 1 支付成功
    private String status;
    //支付订单时间
    private String payOrderTimeStr;

    //支付方式 0 微信支付 1 支付宝支付
    private String payMethod;

    //生成的提供支付厂商的单号
    private String tradeNo;

    //是否已经申述 0否 1是
    private String isState;
    //申述内容
    private String stateReason;
    //状态 0未受理 1已受理
    private String isAcceptance;
    //受理描述
    private String acceptanceStr;
    //逻辑删除
    private String isr;

    //其他类属性
    private MonetaryGoodsDto monetaryGoodsDto;

    private MonetaryAddrDto monetaryAddrDto;

    private UserDTO userDto;
    public MonetaryOrdersDto(){
        System.out.println("111");
    }

    public MonetaryOrdersDto(MonetaryGoodsEntry goodsEntry, MonetaryAddrEntry addrEntry) {
        this.monetaryGoodsDto = new MonetaryGoodsDto(goodsEntry);
        this.monetaryAddrDto = new MonetaryAddrDto(addrEntry);
    }

    public MonetaryOrdersDto(MonetaryGoodsEntry goodsEntry) {
        this.monetaryGoodsDto = new MonetaryGoodsDto(goodsEntry);
    }

    public MonetaryOrdersDto(MonetaryGoodsEntry goodsEntry, MonetaryAddrEntry addressEntry,MonetaryOrdersEntry orderEntry, UserEntry userEntry) {
        this.orderId = orderEntry.getID().toString();
        this.orderNo = orderEntry.getOrderNo();
        this.goodNum = orderEntry.getGoodNum();
        this.excompanyNo = orderEntry.getExcompanyNo();
        this.expressNo = orderEntry.getExpressNo();
        this.isState = orderEntry.getIsState();
        this.stateReason = orderEntry.getStateReason();
        this.goodId = goodsEntry.getID().toString();

        this.isAcceptance = orderEntry.getIsAcceptance();
        this.orderTimeStr = orderEntry.getOrderTimeStr();
        if ("0".equals(orderEntry.getIsAcceptance())) {
            this.acceptanceStr = "未受理";
        } else {
            this.acceptanceStr = "已受理";
        }

        this.monetaryGoodsDto = new MonetaryGoodsDto(goodsEntry);
        this.monetaryAddrDto = new MonetaryAddrDto(addressEntry);
        this.userDto = new UserDTO(userEntry);
    }

    public MonetaryOrdersDto(MonetaryAddrEntry addressEntry,MonetaryOrdersEntry orderEntry) {
        this.orderId = orderEntry.getID().toString();
        this.userId = orderEntry.getUid().toString();
        this.goodId = orderEntry.getGid().toString();
        this.addressId = orderEntry.getAid() == null ? "" : orderEntry.getAid().toString();
        this.goodNum = orderEntry.getGoodNum();
        this.style = orderEntry.getStyle();
        this.money = orderEntry.getMoney();
        this.orderNo = orderEntry.getOrderNo();
        this.excompanyNo = orderEntry.getExcompanyNo();
        this.expressNo = orderEntry.getExpressNo();
        this.orderTimeStr = orderEntry.getOrderTimeStr();
        this.status = orderEntry.getStatus();
        this.payOrderTimeStr = orderEntry.getPayOrderTimeStr();
        this.payMethod = orderEntry.getPayMethod();
        this.tradeNo = orderEntry.getTradeNo();
        if ("0".equals(orderEntry.getIsAcceptance())) {
            this.acceptanceStr = "未受理";
        } else {
            this.acceptanceStr = "已受理";
        }
        if (addressEntry == null){
            this.monetaryAddrDto = new MonetaryAddrDto();
        }else {
            this.monetaryAddrDto = new MonetaryAddrDto(addressEntry);
        }

    }

    public MonetaryOrdersDto(MonetaryOrdersEntry ordersEntry) {
        this.orderId = ordersEntry.getID().toString();
        this.userId = ordersEntry.getUid().toString();
        this.goodId = ordersEntry.getGid().toString();
        this.addressId = ordersEntry.getAid().toString();
        this.goodNum = ordersEntry.getGoodNum();
        this.style = ordersEntry.getStyle();
        this.money = ordersEntry.getMoney();
        this.orderNo = ordersEntry.getOrderNo();
        this.excompanyNo = ordersEntry.getExcompanyNo();
        this.expressNo = ordersEntry.getExpressNo();
        this.orderTimeStr = ordersEntry.getOrderTimeStr();
        this.status = ordersEntry.getStatus();
        this.payOrderTimeStr = ordersEntry.getPayOrderTimeStr();
        this.payMethod = ordersEntry.getPayMethod();
        this.tradeNo = ordersEntry.getTradeNo();
    }

    public MonetaryOrdersDto(MonetaryGoodsEntry goodsEntry, MonetaryOrdersEntry ordersEntry) {
        this.monetaryGoodsDto = new MonetaryGoodsDto(goodsEntry);
        this.orderId = ordersEntry.getID().toString();
        this.userId = ordersEntry.getUid().toString();
        this.goodId = ordersEntry.getGid().toString();
        this.addressId = ordersEntry.getAid().toString();
        this.goodNum = ordersEntry.getGoodNum();
        this.style = ordersEntry.getStyle();
        this.money = ordersEntry.getMoney();
        this.orderNo = ordersEntry.getOrderNo();
        this.excompanyNo = ordersEntry.getExcompanyNo();
        this.expressNo = ordersEntry.getExpressNo();
        this.orderTimeStr = ordersEntry.getOrderTimeStr();
        this.status = ordersEntry.getStatus();
        this.payOrderTimeStr = ordersEntry.getPayOrderTimeStr();
        this.payMethod = ordersEntry.getPayMethod();
        this.tradeNo = ordersEntry.getTradeNo();
    }

    public MonetaryGoodsDto getMonetaryGoodsDto() {
        return monetaryGoodsDto;
    }

    public void setMonetaryGoodsDto(MonetaryGoodsDto monetaryGoodsDto) {
        this.monetaryGoodsDto = monetaryGoodsDto;
    }

    public MonetaryAddrDto getMonetaryAddrDto() {
        return monetaryAddrDto;
    }

    public void setMonetaryAddrDto(MonetaryAddrDto monetaryAddrDto) {
        this.monetaryAddrDto = monetaryAddrDto;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
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

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public String getPayOrderTimeStr() {
        return payOrderTimeStr;
    }

    public void setPayOrderTimeStr(String payOrderTimeStr) {
        this.payOrderTimeStr = payOrderTimeStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getIsState() {
        return isState;
    }

    public void setIsState(String isState) {
        this.isState = isState;
    }

    public String getStateReason() {
        return stateReason;
    }

    public void setStateReason(String stateReason) {
        this.stateReason = stateReason;
    }

    public String getIsAcceptance() {
        return isAcceptance;
    }

    public void setIsAcceptance(String isAcceptance) {
        this.isAcceptance = isAcceptance;
    }

    public String getAcceptanceStr() {
        return acceptanceStr;
    }

    public void setAcceptanceStr(String acceptanceStr) {
        this.acceptanceStr = acceptanceStr;
    }

    public UserDTO getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDTO userDto) {
        this.userDto = userDto;
    }

    public String getIsr() {
        return isr;
    }

    public void setIsr(String isr) {
        this.isr = isr;
    }
}
