package com.pojo.ebusiness;


import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;

/**
 * 订单地址
 * <pre>
 * collectionName:eoaddress
 * </pre>
 * <pre>
 * {
     p:省
     ci:市
     xi:县
     ui:用户ID
     u:用户
     add:地址
     tel:电话
     def:是否为默认地址 0：非默认 1：默认
 * }
 * </pre>
 * @author fourer
 */
public class EOrderAddressEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2258729165619111681L;
	
	
	public EOrderAddressEntry(){}
	
	public EOrderAddressEntry(BasicDBObject baseEntry) {
		super(baseEntry);
		// TODO Auto-generated constructor stub
	}


	public EOrderAddressEntry(ObjectId ui, String user, String address, String telephone) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("p", null)
		.append("ci",null)
		.append("xi", null)
		.append("ui", ui)
		.append("u", user)
		.append("add", address)
		.append("tel", telephone)
		.append("def", 0);
		setBaseEntry(baseEntry);
	}

	public EOrderAddressEntry(IdValuePair province, IdValuePair city,
			IdValuePair xian,ObjectId ui, String user, String address, String telephone) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("p", province.getBaseEntry())
		.append("ci", city.getBaseEntry())
		.append("xi", xian.getBaseEntry())
		.append("ui", ui)
		.append("u", user)
		.append("add", address)
		.append("tel", telephone);
		setBaseEntry(baseEntry);
	}

	public EOrderAddressEntry(String province, String city,
							  String xian,ObjectId ui, String user, String address, String telephone, int isDefault) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
				.append("p", province)
				.append("ci", city)
				.append("xi", xian)
				.append("ui", ui)
				.append("u", user)
				.append("add", address)
				.append("tel", telephone)
				.append("def", isDefault);
		setBaseEntry(baseEntry);
	}


	public String getProvince() {
		/*BasicDBObject province =(BasicDBObject)getSimpleObjectValue("p");
		if(null!=province)
		{
			return new IdValuePair(province);
		}
		return null;*/
		if(getBaseEntry().containsField("p")){
			return getSimpleStringValue("p") == null ? "" : getSimpleStringValue("p");
		}

		return "";
	}


	public void setProvince(String province) {
		setSimpleValue("p", province);
	}



	public String getCity() {
		if(getBaseEntry().containsField("ci")){
			return getSimpleStringValue("ci") == null ? "" : getSimpleStringValue("ci");
		}

		return "";
	}

	public void setCity(String city) {
		setSimpleValue("ci", city);
	}



	public String getXian() {
		if(getBaseEntry().containsField("xi")){
			return getSimpleStringValue("xi") == null ? "" : getSimpleStringValue("xi");
		}

		return "";
	}



	public void setXian(String xian) {
		setSimpleValue("xi", xian);
	}

	public String getUser() {
		return getSimpleStringValue("u");
	}

	public void setUser(String user) {
		setSimpleValue("u", user);
	}

	
	public ObjectId getUserID() {
		return getSimpleObjecIDValue("ui");
	}

	public void setUserID(ObjectId user) {
		setSimpleValue("ui", user);
	}
	public String getAddress() {
		return getSimpleStringValue("add");
	}

	public void setAddress(String address) {
		setSimpleValue("add", address);
	}

	public String getTelephone() {
		return getSimpleStringValue("tel");
	}

	public void setTelephone(String telephone) {
		setSimpleValue("tel", telephone);
	}

	public int getIsDefault(){
		if(getBaseEntry().containsField("def")){
			return getSimpleIntegerValue("def");
		}
		return 0;
	}

	public void setIsDefault(int isDefault){
		setSimpleValue("def",isDefault);
	}

}
