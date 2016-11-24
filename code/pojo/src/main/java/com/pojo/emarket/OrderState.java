package com.pojo.emarket;

/**
 * 订单状态
 * @author fourer
 *
 */
public enum OrderState {

	READY(1,"刚刚下单"),
	PAYED(2,"已经付款"),
	COMPLETE(3,"已经完成"),
	ROVOKE(4,"已经撤销"),
	DELETE(5,"已经删除"),
	DELIVERED(6, "已经发货"),
	;
	
	
	private int type; 
	private String name;
	
	private OrderState(int type, String name) {
		this.name = name;
		this.type = type;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	public static OrderState getOrderState(int type)
	{
		for(OrderState orderState:OrderState.values())
		{
			if(orderState.getType()==type)
			{
				return orderState;
			}
		}
		
		return null;
	}
}
