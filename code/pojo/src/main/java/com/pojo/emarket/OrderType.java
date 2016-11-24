package com.pojo.emarket;

public enum OrderType {
	COURSE(0,"课程订单"),
	RECHARGE(2,"充值订单"),
	QUESTION(3,"试题订单"),
	EXCELLENTLESSON(4,"精品课程订单");
	
	private int status;
	private String description;
	
	private OrderType(int status,String description){
		this.status = status;
		this.description = description;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static OrderType getOrderType(int type){
		for (OrderType orderType : OrderType.values()) {
			if(orderType.getStatus() == type){
				return orderType;
			}
		}
		return null;
	}
}
