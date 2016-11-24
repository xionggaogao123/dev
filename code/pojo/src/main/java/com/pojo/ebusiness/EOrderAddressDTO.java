package com.pojo.ebusiness;


import org.bson.types.ObjectId;

/**
 * 订单地址
 * @author fourer
 *
 */

public class EOrderAddressDTO {

	private String id;
	private String userName;
	private String address;
	private String telephone;
	private String province;
	private String city;
	private String district;
	private String isDefault;//0:非默认，1:默认
	
	
	public EOrderAddressDTO (EOrderAddressEntry e)
	{
		this.id=e.getID().toString();
		this.userName=e.getUser();
		this.address=e.getAddress();
		this.telephone=e.getTelephone();
		this.province = e.getProvince();
		this.city = e.getCity();
		this.district = e.getXian();
		this.isDefault = String.valueOf(e.getIsDefault());
	}
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}
