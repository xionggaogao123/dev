package com.pojo.ebusiness;

import com.pojo.ebusiness.EOrderEntry.EOrderGoods;

public class EBusinessOrderGoods extends EBusinessCartGoods {

	/**
	 * 
	 */
	private static final long serialVersionUID = 729749002093652261L;
	private int state;
	private String message;

	public EBusinessOrderGoods(  EOrderEntry eoe,EOrderGoods ge, EGoodsEntry ee) {
		super(ge, ee);
		this.state=eoe.getState();
		this.message = ge.getMessage();
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public String getStateStr() {
		if(this.state==1) {
			return "待付款";
		} else if(this.state==2) {
			return "待发货";
		} else if(this.state==3) {
			return "已完成";
		} else if(this.state==4) {
			return "已撤销";
		} else if(this.state == 6){
			return "已发货";
		}
		return "已删除";
	}
	public void setStateStr(String stateStr) {
		//this.stateStr = stateStr;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
