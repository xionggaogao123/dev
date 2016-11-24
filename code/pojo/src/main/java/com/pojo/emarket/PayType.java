package com.pojo.emarket;
/**
 * 支付类型
 * @author fourer
 *
 */
public enum PayType {

	NONE(0,"未支付"),
	ONLINE(1,"余额支付"),
	ALIPAY(2,"支付宝支付"),
	;

	private int type; 
	private String name;
	
	private PayType(int type, String name) {
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
	
	public static PayType getPayType(int type){
		for(PayType payType:PayType.values()){
			if(payType.getType() == type){
				return payType;
			}
		}
		return null;
	}
}
