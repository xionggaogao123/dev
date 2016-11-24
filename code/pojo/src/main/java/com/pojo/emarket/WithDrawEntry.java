package com.pojo.emarket;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;

public class WithDrawEntry extends BaseDBObject {

	private static final long serialVersionUID = 6539742128578204451L;
	
	public WithDrawEntry(DBObject dbo){
		super((BasicDBObject) dbo);
	}
	
	public WithDrawEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public WithDrawEntry(String userid, String paypalAccount, double cash,
			long createTime, int isPay, long updateTime, String username,
			String password, String openBank, String phone, int payType,String orderid,String cardNum) {
		super();
		BasicDBObject dbo = new BasicDBObject()
								.append("uid", userid)
								.append("pa", paypalAccount)
								.append("cs", cash)
								.append("ct", createTime)
								.append("isp", isPay)
								.append("ut", updateTime)
								.append("un", username)
								.append("pw", password)
								.append("ob", openBank)
								.append("ph", phone)
								.append("pt", payType)
								.append("orid",orderid)
								.append("cd",cardNum)
								.append("st",0)
								.append("bz", "");
		setBaseEntry(dbo);
	}
	public String getUserid() {
		return getSimpleStringValue("uid");
	}
	public void setUserid(String userid) {
		setSimpleValue("uid", userid);
	}
	public String getPaypalAccount() {
		return getSimpleStringValue("pa");
	}
	public void setPaypalAccount(String paypalAccount) {
		setSimpleValue("pa", paypalAccount);
	}
	public double getCash() {
		return getSimpleDoubleValue("cs");
	}
	public void setCash(double cash) {
		setSimpleValue("cs", cash);
	}
	public long getCreateTime() {
		return getSimpleLongValue("ct");
	}
	public void setCreateTime(long createTime) {
		setSimpleValue("ct", createTime);
	}
	public int getIsPay() {
		return getSimpleIntegerValue("isp");
	}
	public void setIsPay(int isPay) {
		setSimpleValue("isp", isPay);
	}
	public long getUpdateTime() {
		return getSimpleLongValue("ut");
	}
	public void setUpdateTime(long updateTime) {
		setSimpleValue("up", updateTime);
	}
	public String getUsername() {
		return getSimpleStringValue("un");
	}
	public void setUsername(String username) {
		setSimpleValue("un", username);
	}
	public String getPassword() {
		return getSimpleStringValue("pw");
	}
	public void setPassword(String password) {
		setSimpleValue("pw", password);
	}
	public String getOpenBank() {
		return getSimpleStringValue("ob");
	}
	public void setOpenBank(String openBank) {
		setSimpleValue("ob", openBank);
	}
	public String getPhone() {
		return getSimpleStringValue("ph");
	}
	public void setPhone(String phone) {
		setSimpleValue("ph", phone);
	}
	public int getPayType() {
		return getSimpleIntegerValue("pt");
	}
	public void setPayType(int payType) {
		setSimpleValue("pt", payType);
	}
	public String getOrderid() {
		return getSimpleStringValue("orid");
	}
	public void setOrderid(String orderid) {
		setSimpleValue("orid", orderid);
	}
	public String getCardNum() {
		return getSimpleStringValue("cd");
	}
	public void setCardNum(String cardNum) {
		setSimpleValue("cd",cardNum);
	}
	public int getStatus() {
		return getSimpleIntegerValue("st");
	}
	public void setStatus(int status) {
		setSimpleValue("st",status);
	}
	public String getBeiZhu() {
		return getSimpleStringValue("bz");
	}
	public void setBeiZhu(String beiZhu) {
		setSimpleValue("bz",beiZhu);
	}
}
