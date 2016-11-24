package com.pojo.ebusiness;

import java.util.ArrayList;
import java.util.List;

import com.sys.utils.DateTimeUtils;

/**
 * 商品订单信息
 * @author fourer
 *
 */
public class EOrderInfoDTO {

	private String id;
	private String date;
	private String orderNumber;
	private List<EBusinessOrderGoods> goodsList =new ArrayList<EBusinessOrderGoods>();
	private String phoneNumber;
	private String userName;
	private String address;
	private String receiver;
	private int expOff;
	private int voucherOff;
	private int state;
	private int expressPrice;
	private int totalPrice;
	private String province;
	private String city;
	private String district;
	
	
	public EOrderInfoDTO (EOrderEntry e)
	{
		this.id=e.getID().toString();
		this.orderNumber=e.getEorderNumber();
		this.date=DateTimeUtils.convert(e.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
		this.expOff = e.getUsedExp() * 10;
		this.voucherOff = e.getVoucherOff();
		this.state = e.getState();
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<EBusinessOrderGoods> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<EBusinessOrderGoods> goodsList) {
		this.goodsList = goodsList;
	}
	
	
	
	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public void addEBusinessCartGoods(EBusinessOrderGoods ecgs)
	{
		this.goodsList.add(ecgs);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getExpOff() {
		return expOff;
	}

	public void setExpOff(int expOff) {
		this.expOff = expOff;
	}

	public int getVoucherOff() {
		return voucherOff;
	}

	public void setVoucherOff(int voucherOff) {
		this.voucherOff = voucherOff;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getExpressPrice() {
		return expressPrice;
	}

	public void setExpressPrice(int expressPrice) {
		this.expressPrice = expressPrice;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
}
