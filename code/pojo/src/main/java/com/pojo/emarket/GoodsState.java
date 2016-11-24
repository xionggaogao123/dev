package com.pojo.emarket;

/**
 * 商品状态
 * @author fourer
 *
 */
public enum GoodsState {

	SHELVE(1,"上架"),
	UNSHELVE(2,"下架"),
	DELETE(3,"删除"),
	;
	
	private int type; 
	private String name;
	
	
	
	private GoodsState(int type, String name) {
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
	
}
