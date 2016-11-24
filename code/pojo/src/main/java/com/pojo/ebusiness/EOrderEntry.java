package com.pojo.ebusiness;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 商品订单
 * <pre>
 * collectionName:eorder
 * </pre>
 * <pre>
 * {
    ui:用户ID
    ogs:
    [
     {
	     egi:商品ID
	     kis[]:规格ID
	     c:数量
	     pr:单价
	     me:留言
		 ecn:快递公司编号
		 en:快递单号
	  }
    ]
     add:送货地址
     pt:支付方式 0未支付 1支付宝 2微信 
     tpr:总价
     pn：支付单号
     st:之前状态 0已下单，未支付 1已经支付，没有送货 3已经送货，交易完成  
                       修改之后参见OrderState
     ern:订单单号
     exp:使用的积分 抵用价格为exp*10(分)
     vid:抵用券id
     voff:抵用券抵用的价格（分）
 	 ep:运费
 	 pid:parentId 关联家长id
 * }
 * </pre>
 * @author fourer
 */
public class EOrderEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6337766844764760597L;
	
	public EOrderEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	
	
	public EOrderEntry(ObjectId userId, ObjectId addressId,int payType, String payNumber,int state,List<EOrderGoods> orderGoods,
					   String eorderNumber, int usedExp, ObjectId voucherId, int voucherOff,int expressPrice,ObjectId parentId) {
		int price=0;
		for(EOrderGoods eg:orderGoods)
		{
			price+=eg.getPrice()*eg.getCount();
		}
		//商品总价满99包邮
		if(price >= 9900){
			expressPrice = 0;
		}
		price = price  - usedExp * 10 - voucherOff;
		if(price < 0){
			price = 0;
		}
		price += expressPrice;
		List<DBObject> ls =MongoUtils.fetchDBObjectList(orderGoods);
		BasicDBObject baseEntry=new BasicDBObject()
				.append("ui", userId)
				.append("add", addressId)
				.append("pt", payType)
				.append("pn", payNumber)
				.append("st", state)
				.append("ogs", MongoUtils.convert(ls))
				.append("tpr", price)
				.append("ern", eorderNumber)
				.append("exp", usedExp)
				.append("vid", voucherId)
				.append("voff", voucherOff)
				.append("ep", expressPrice)
				.append("pid",parentId);

		setBaseEntry(baseEntry);
	}


	
	public String getEorderNumber() {
		String ern= getSimpleStringValue("ern");
		if(StringUtils.isBlank(ern))
		{
			ern=getID().toString();
		}
		return ern;
	}



	public void setEorderNumber(String eorderNumber) {
		setSimpleValue("ern", eorderNumber);
	}



	public ObjectId getAddressId() {
		return getSimpleObjecIDValue("add");
	}

	public void setAddressId(ObjectId addressId) {
		setSimpleValue("add", addressId);
	}


	public int getTotalPrice() {
		return getBaseEntry().containsField("tpr") ? getSimpleIntegerValue("tpr") : 0;

	}
	public void setTotalPrice(int totalPrice) {
		setSimpleValue("tpr", totalPrice);
	}

	public List<EOrderGoods> getOrderGoods() {
		List<EOrderGoods> retList =new ArrayList<EOrderGoods>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ogs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add( new EOrderGoods((BasicDBObject)o)   );
			}
		}
		return retList;
	}

	public void setOrderGoods(List<EOrderGoods> orderGoods) {
		List<DBObject> ls =MongoUtils.fetchDBObjectList(orderGoods);
		setSimpleValue("ogs", MongoUtils.convert(ls));
	}

	public String getPayNumber() {
		return getSimpleStringValue("pn");
	}

	public void setPayNumber(String payNumber) {
		setSimpleValue("pn", payNumber);
	}
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}
	
	
	
	
	
	public int getPayType() {
		return getSimpleIntegerValue("pt");
	}
	public void setPayType(int payType) {
		setSimpleValue("pt", payType);
	}
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public int getUsedExp(){
		return getSimpleIntegerValueDef("exp", 0);
	}

	public void setUsedExp(int usedExp){
		setSimpleValue("exp", usedExp);
	}

	public ObjectId getVoucherId(){
		if(getBaseEntry().containsField("vid")){
			return getSimpleObjecIDValue("vid");
		} else {
			return null;
		}
	}

	public void setVoucherId(ObjectId voucherId){
		setSimpleValue("vid", voucherId);
	}

	public int getVoucherOff(){
		return getSimpleIntegerValueDef("voff", 0);
	}

	public void setVoucherOff(int voucherOff){
		setSimpleValue("voff", voucherOff);
	}
	
	public int getExpressPrice(){
		int ep = 0;
		if(getBaseEntry().containsField("ep")){
			ep = getSimpleIntegerValue("ep");
		}
		return ep;
	}

	public void setExpressPrice(int expressPrice){
		setSimpleValue("ep",expressPrice);
	}

	public ObjectId getParentId(){
		if(getBaseEntry().containsField("pid")){
			return getSimpleObjecIDValue("pid");
		}
		return null;
	}

	public void setParentId(ObjectId parentId){
		setSimpleValue("pid",parentId);
	}

	/**
	 * 订单商品
	 * <pre>
	 * collectionName:eorder
	 * </pre>
	 * <pre>
	 * {
	     egi:商品ID
	     kis[]:规格ID
	     c:数量
	     pr:单价
	     me:留言
	 	 ecn:快递公司编号
	     en:快递单号
	 * }
	 * </pre>
	 * @author fourer
	 */
	public static class EOrderGoods extends BaseDBObject
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8242411178317315241L;
		
		
		public EOrderGoods(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		
		public EOrderGoods(ObjectId eGoodsId,List<ObjectId> kindsList,int price,int count,String message,String exCompanyNo,String expressNo) {
			
			BasicDBObject baseEntry=new BasicDBObject()
			.append("egi", eGoodsId)
			.append("kis", MongoUtils.convert(kindsList))
			.append("pr", price)
			.append("c", count)
			.append("me", message)
			.append("ecn", exCompanyNo)
			.append("en", expressNo)
			;
			setBaseEntry(baseEntry);
		}
		

		
		public ObjectId geteGoodsId() {
			return getSimpleObjecIDValue("egi");
		}
		public void seteGoodsId(ObjectId eGoodsId) {
			setSimpleValue("egi", eGoodsId);
		}
		
		public int getPrice() {
			return getSimpleIntegerValue("pr");
		}
		public void setPrice(int price) {
			setSimpleValue("pr", price);
		}
		public int getCount() {
			return getSimpleIntegerValue("c");
		}
		public void setCount(int count) {
			setSimpleValue("c", count);
		}

		public String getExCompanyNo(){
			String ecn = getSimpleStringValue("ecn");
			return getBaseEntry().containsField("ecn") ? ecn : "";
		}
		public void setExCompnayNo(String exCompnayNo){
			setSimpleValue("ecn",exCompnayNo);
		}
		public String getExpressNo(){
			String en = getSimpleStringValue("en");
			return getBaseEntry().containsField("ecn") ? en : "";
		}
		public void setExpressNo(String expressNo){
			setSimpleValue("en", expressNo);
		}
		
		public String getMessage() {
			return getSimpleStringValue("me");
		}
		public void setMessage(String message) {
			setSimpleValue("me", message);
		}
		
		public List<ObjectId> getKindIds() {
			List<ObjectId> retList =new ArrayList<ObjectId>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("kis");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add((ObjectId)o);
				}
			}
			return retList;
		}
		public void setKindIds(List<ObjectId> kindIds) {
			setSimpleValue("kis", MongoUtils.convert(kindIds));
		}
	}

}
