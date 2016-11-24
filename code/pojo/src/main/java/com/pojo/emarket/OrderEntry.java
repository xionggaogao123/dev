package com.pojo.emarket;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 订单表
 * <pre>
 * collectionName:orders
 * </pre>
 * <pre>
 * {
 *    sub:科目ID；对应SubjectType
      ccgts:年级；对应GradeType
      [
         
      ]
      onum:订单编号
      ui:
      {
       id:
       v: 用户名称
      }
      gty:商品种类 ；参见GoodsType
      gi:{
        id:
        v:商品名称
      }
      pr:交易价格
      pat:支付类型
      st:状态 ;参见OrderState
      lut:最后更新时间
      ot:订单类型;参见OrderType
      oi:
      {
      	id:
      	v:拥有者名称
      }
	 et:有效时间 // long
 * }
 * </pre>
 * @author fourer
 */
public class OrderEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4629568827512021658L;

	public OrderEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public OrderEntry(String ordernum,int subject, List<Integer> cloudClassGradeTypes,IdValuePair userInfo,GoodsType type, IdValuePair goodsInfo,PayType paytype,double price,OrderType orderType, IdValuePair ownerInfo,long expireTime)
	{
		this(ordernum,subject,cloudClassGradeTypes,userInfo,type, goodsInfo, OrderState.READY.getType(), System.currentTimeMillis(),paytype,price, orderType,ownerInfo,expireTime);
	}
	
	public OrderEntry(String ordernum,int subject, List<Integer> cloudClassGradeTypes,IdValuePair userInfo,GoodsType type, IdValuePair goodsInfo, int state,PayType paytype,double price,OrderType orderType, IdValuePair ownerInfo,long expireTime)
	{
		this(ordernum,subject,cloudClassGradeTypes,userInfo,type, goodsInfo, state, System.currentTimeMillis(),paytype, price, orderType,ownerInfo,expireTime);
	}
	
	public OrderEntry(String ordernum,int subject, List<Integer> cloudClassGradeTypes,IdValuePair userInfo,GoodsType type, IdValuePair goodsInfo, int state,
			long lastUpdateTime,PayType paytype,double price,OrderType orderType, IdValuePair ownerInfo,long expireTime) {
		super();
		BasicDBObject dbo =new BasicDBObject().append("onum",ordernum)
		.append("sub", subject)
		.append("ccgts", MongoUtils.convert(cloudClassGradeTypes))
		.append("ui", userInfo.getBaseEntry())
		.append("gty", type.getType())
		.append("gi", goodsInfo.getBaseEntry())
		.append("st", state)
		.append("lut", lastUpdateTime)
		.append("pat", paytype.getType())
		.append("pr", price)
		.append("ot", orderType.getStatus())
		.append("oi", ownerInfo.getBaseEntry())
		.append("et", expireTime)
		;
		setBaseEntry(dbo);
	}
	
	public int getOrderType() {
		return getSimpleIntegerValue("ot");
	}

	public void setOrderType(OrderType orderType) {
		setSimpleValue("ot", orderType.getStatus());
	}

	public int getSubject() {
		return getSimpleIntegerValue("sub");
	}
	public void setSubject(int subject) {
		setSimpleValue("sub", subject);
	}
	
	public List<Integer> getCloudClassGradeTypes() {
		List<Integer> retList =new ArrayList<Integer>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ccgts");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((Integer)o);
			}
		}
		return retList;
	}
	
	public double getPrice() {
		return getSimpleDoubleValue("pr");
	}

	public void setPrice(double price) {
		setSimpleValue("pr", price);
	}

	public String getOrdernum() {
		return getSimpleStringValue("onum");
	}

	public void setOrdernum(String ordernum) {
		setSimpleValue("onum",ordernum);
	}

	public int getPayType() {
		return getSimpleIntegerValue("pat");
	}

	public void setPayType(int payType) {
		setSimpleValue("pat", payType);
	}

	public int getGoodsType() {
		return getSimpleIntegerValue("gty");
	}

	public void setGoodsType(int goodsType) {
		setSimpleValue("gty", goodsType);
	}

	public long getExpireTime() {
		return getSimpleLongValue("et");
	}

	public void setExpireTime(long expireTime) {
		setSimpleValue("et",expireTime);
	}
	
	public IdValuePair getUserInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("ui");
		if(null!=dbo)
			return new IdValuePair(dbo);
		return null;
	}

	public void setUserInfo(IdValuePair userInfo) {
		setSimpleValue("ui", userInfo.getBaseEntry());
	}

	public IdValuePair getGoodsInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("gi");
		if(null!=dbo)
			return new IdValuePair(dbo);
		return null;
	}

	public void setGoodsInfo(IdValuePair goodsInfo) {
		setSimpleValue("gi", goodsInfo.getBaseEntry());
	}

	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public long getLastUpdateTime() {
		return getSimpleLongValue("lut");
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		setSimpleValue("lut", lastUpdateTime);
	}
	
	public IdValuePair getOwnerInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("oi");
		if(null!=dbo)
			return new IdValuePair(dbo);
		return null;
	}

	public void setOwnerInfo(IdValuePair ownerInfo) {
		setSimpleValue("oi", ownerInfo.getBaseEntry());
	}
	
}
