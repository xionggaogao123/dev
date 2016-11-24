package com.pojo.emarket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pojo.app.IdValuePair;


/**
 * 商品DTO
 * @author fourer
 *
 */
public class OrderDTO {

	private String id;
	private String orderid;
	private int subject;
	private List<Integer> cloudClassGradeTypes;
	private IdValuePair userInfo;
	private int type;
	private IdValuePair goodsInfo;
	private int state;
	private String lastUpdateTime;
	private int payType;
	private double price;
	private int orderType;
	private long expire;
	private int expireStatus;
	
	public OrderDTO(OrderEntry entry,long expiretime)
	{
		this.id = entry.getID().toString();
		this.orderid = entry.getOrdernum();
		this.subject = entry.getSubject();
		this.cloudClassGradeTypes = entry.getCloudClassGradeTypes();
		this.userInfo = entry.getUserInfo();
		this.type = entry.getGoodsType();
		this.goodsInfo = entry.getGoodsInfo();
		this.state = entry.getState();
		this.lastUpdateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(entry.getLastUpdateTime()));
		this.payType = entry.getPayType();
		this.price = entry.getPrice();
		this.orderType = entry.getOrderType();
		this.expire = expiretime;
		if((new Date(entry.getExpireTime()).compareTo(new Date())==1) || (new Date(entry.getExpireTime()).compareTo(new Date())==0)) {
			this.expireStatus = 2;
		} else if (new Date(entry.getExpireTime()).compareTo(new Date())==-1) {
			this.expireStatus = 1;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSubject() {
		return subject;
	}

	public void setSubject(int subject) {
		this.subject = subject;
	}

	public List<Integer> getCloudClassGradeTypes() {
		return cloudClassGradeTypes;
	}

	public void setCloudClassGradeTypes(List<Integer> cloudClassGradeTypes) {
		this.cloudClassGradeTypes = cloudClassGradeTypes;
	}

	public IdValuePair getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(IdValuePair userInfo) {
		this.userInfo = userInfo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public IdValuePair getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(IdValuePair goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public int getExpireStatus() {
		return expireStatus;
	}

	public void setExpireStatus(int expireStatus) {
		this.expireStatus = expireStatus;
	}
}
