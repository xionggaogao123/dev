package com.pojo.emarket;

/**
 * 商品种类
 * @author fourer
 *
 */
public enum GoodsType {
	LESSON(1,"课程"),
	BOOK(2,"书籍"),
	ACCOUNT(3,"账户"),
	;
	private int type; 
	private String name;
	
	
	
	private GoodsType(int type, String name) {
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
	
	
	public static GoodsType getGoodsType(int type)
	{
		for(GoodsType goodsType:GoodsType.values())
		{
			if(goodsType.getType()==type)
			{
				return goodsType;
			}
		}
		
		return null;
	}
	
}
