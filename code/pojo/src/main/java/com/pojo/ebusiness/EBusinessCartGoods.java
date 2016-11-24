package com.pojo.ebusiness;

import java.io.Serializable;
import org.bson.types.ObjectId;
import com.pojo.ebusiness.EOrderEntry.EOrderGoods;


/**
 * 购物车商品
 * @author fourer
 *
 */
public class EBusinessCartGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3721360269544746118L;
	//private static DecimalFormat df1 = new DecimalFormat("####.00");
	private String ebcId;//ID表示
	private String id; //商品ID
	private String pinpai;
	private String image;
	private String name;
	private String kind;
	private String objKinds;
	private int price;
	private int count;
	private int type; //0 没有购买 1 已经购买
	private  String exCompanyNo;
	private  String expressNo;
	private int experienceOff;//单位 分
	private int voucherOff;//单位  分
	private String exTempId;
	private int state;
	//实付金额
    private String priceDisCount;
	private int discountPrice;
	private int realPrice;
	private String priceReal;

	//抵用金额
	private String pricel;



	public EBusinessCartGoods(){}
	
	public EBusinessCartGoods(EGoodsEntry ge,int count)
	{
		this.ebcId=new ObjectId().toString();
		this.pinpai=ge.getPinpai();
		this.id=ge.getID().toString();
		this.image=ge.getSuggestImage().toString();
		this.name=ge.getName();
		this.price=ge.getDiscountPrice();
		this.count=count;
		this.experienceOff = ge.getExperienceOff();
		this.voucherOff = ge.getVoucherOff();
		this.exTempId = ge.getExpTempId() == null ? "" : ge.getExpTempId().toString();
	}
	
	
	public EBusinessCartGoods(EOrderGoods ge,EGoodsEntry ee)
	{
		this.ebcId=new ObjectId().toString();
		this.pinpai=ee.getPinpai();
		this.id=ee.getID().toString();
		this.image=ee.getSuggestImage().toString();
		this.name=ee.getName();
		this.price=ge.getPrice();
		this.discountPrice=ee.getDiscountPrice();
		this.realPrice=ee.getPr();
		this.count=ge.getCount();
		this.exCompanyNo=ge.getExCompanyNo();
		this.expressNo=ge.getExpressNo();
//		this.priceCount=ee.getDiscountPrice();
		if(ee.getExpTempId() == null){
			this.exTempId = "";
		}else{
			this.exTempId = ee.getExpTempId().toString();
		}
		this.state = ee.getState();
	}
	
	
	
	
	public String getPinpai() {
		return pinpai;
	}
	public void setPinpai(String pinpai) {
		this.pinpai = pinpai;
	}
	public String getEbcId() {
		return ebcId;
	}
	public void setEbcId(String ebcId) {
		this.ebcId = ebcId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getExCompanyNo(){
		return  exCompanyNo;
	}
	public void SetExCompanyNo(String exCompanyNo){
		this.exCompanyNo = exCompanyNo;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getObjKinds() {
		return objKinds;
	}
	public void setObjKinds(String objKinds) {
		this.objKinds = objKinds;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public String getPriceStr1()
	{
		if(this.price<=0)
		{
			return "0.00";
		}
		
		Double d=Double.valueOf(String.valueOf(this.price))/100D;
		return d.toString();
	}

	public String getPriceStr() {
		if(this.price<=0)
		{
			return "0.00";
		}
		
		Double d=Double.valueOf(String.valueOf(this.price*this.count))/100D;
		return d.toString();
		
	}



	public void setPriceStr(String priceStr) {
		
	}

	public String getPriceDisCount() {
		if(this.discountPrice<=0)
		{
			return "0.00";
		}

		Double d=Double.valueOf(String.valueOf(this.discountPrice*this.count))/100D;
		return d.toString();
	}

	public void setPriceDisCount(String priceDisCount) {
		this.priceDisCount = priceDisCount;
	}

	public String getPriceReal() {
		if(this.realPrice<=0)
		{
			return "0.00";
		}

		Double d=Double.valueOf(String.valueOf(this.realPrice*this.count))/100D;
		return d.toString();
	}

	public void setPriceReal(String priceReal) {
		this.priceReal = priceReal;
	}
	public int getExperienceOff() {
		return experienceOff;
	}

	public void setExperienceOff(int experienceOff) {
		this.experienceOff = experienceOff;
	}

	public int getVoucherOff() {
		return voucherOff;
	}

	public void setVoucherOff(int voucherOff) {
		this.voucherOff = voucherOff;
	}

	public void setExCompanyNo(String exCompanyNo) {
		this.exCompanyNo = exCompanyNo;
	}

	public String getExTempId() {
		return exTempId;
	}

	public void setExTempId(String exTempId) {
		this.exTempId = exTempId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(int realPrice) {
		this.realPrice = realPrice;
	}

	public String getPricel() {
		if(this.realPrice<=0)
		{
			return "0.00";
		}

		Double d=Double.valueOf(String.valueOf(this.realPrice*this.count-this.discountPrice*this.count))/100D;
		return d.toString();
	}

	public void setPricel(String pricel) {
		this.pricel = pricel;
	}

	@Override
	public String toString() {
		return "EBusinessCartGoods [ebcId=" + ebcId + ", id=" + id
				+ ", pinpai=" + pinpai + ", image=" + image + ", name=" + name
				+ ", kind=" + kind + ", objKinds=" + objKinds + ", price="
				+ price + ", count=" + count + ", type=" + type + "]";
	}
	
}
