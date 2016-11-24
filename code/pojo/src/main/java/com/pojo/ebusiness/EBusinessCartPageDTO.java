package com.pojo.ebusiness;


import java.util.ArrayList;
import java.util.List;
/**
 * 购物车页面DTO
 * @author fourer
 *
 */
public class EBusinessCartPageDTO {

	private int count;
	private int price;
	private List<EBusinessCartGoods> list =new ArrayList<EBusinessCartGoods>();

	//private static DecimalFormat df1 = new DecimalFormat("####.00");
	
	public EBusinessCartPageDTO(List<EBusinessCartGoods> list)
	{
		this.list=list;
		for(EBusinessCartGoods ecg:list)
		{
			this.count+=ecg.getCount();
			this.price+=ecg.getPrice()*ecg.getCount();
		}
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public List<EBusinessCartGoods> getList() {
		return list;
	}
	public void setList(List<EBusinessCartGoods> list) {
		this.list = list;
	}

	public String getPriceStr() {
		
		
		if(this.price<=0)
		{
			return "0.00";
		}
		Double d=Double.valueOf(String.valueOf(this.price))/100D;
		return d.toString();
		//return df1.format(Double.valueOf(String.valueOf(this.price))/100);
	}

	public void setPriceStr(String priceStr) {
		
	}
	
	
}
