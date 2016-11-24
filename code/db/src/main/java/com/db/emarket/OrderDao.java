package com.db.emarket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;
import com.pojo.emarket.GoodsType;
import com.pojo.emarket.OrderEntry;
import com.pojo.emarket.OrderState;
import com.pojo.emarket.OrderType;
import com.sys.constants.Constant;

/**
 * 订单操作类
 * @author fourer
 *
 */
public class OrderDao extends BaseDao {

	/**
	 * 添加订单
	 * @param e
	 * @return
	 */
	public ObjectId addOrderEntry(OrderEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	
	/**
	 * 更新订单状态
	 * @param id
	 * @param updateToState
	 */
	public void updateState(ObjectId id,OrderState updateToState)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st",updateToState.getType()).append("lut", System.currentTimeMillis()));             
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, updateValue);
	}
	
	/**
	 * 得到数量
	 * @param sub
	 * @param gradeType
	 * @param user
	 * @param type
	 * @param state
	 * @return
	 */
	public int count(int sub, int gradeType,ObjectId user,GoodsType type,OrderState state, OrderType orderType)
	{
		DBObject query=  buildQuery(sub, gradeType, user, type, state, orderType);
		
		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query);
	}
	/**
	 * 
	 * @param user
	 * @param type
	 * @param state
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<OrderEntry> getUserOrderList(int sub, int gradeType,ObjectId user,GoodsType type,OrderState state,OrderType orderType,int skip,int limit)
	{
		List<OrderEntry> retList =new ArrayList<OrderEntry>();
		DBObject query=  buildQuery(sub, gradeType, user, type, state, orderType);
		
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, Constant.FIELDS, new BasicDBObject("lut",Constant.DESC), skip, limit);
		if(null!=list && !list.isEmpty())
		{
			for(DBObject dbo:list)
			{
				retList.add(new OrderEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}
	
	/**
	 * 根据条件查询销售订单
	 * @param lessonName
	 * @param startTime
	 * @param endTime
	 * @param nickName
	 * @param state
	 * @return
	 */
	public List<OrderEntry> getUserOrderlist(ObjectId userId, String lessonName, long startTime, long endTime, String nickName, OrderState state, int skip, int limit){
		List<OrderEntry> retList = new ArrayList<OrderEntry>();
		BasicDBObject query = new BasicDBObject();
		if(null != userId){
			query.append("oi.id", userId);
		}
		if(null != lessonName && lessonName.length() != 0){
			query.append("gi.v", lessonName);
		}
		if(0 != startTime){
			query.append("lut", new BasicDBObject(QueryOperators.GTE, startTime));
		}
		if(0 != endTime){
			query.append("lut", new BasicDBObject(QueryOperators.LTE, endTime+86400000));
		}
		if(null != nickName && nickName.length() != 0){
			query.append("ui.v", nickName);
		}
		if(null != state){
			query.append("st", state.getType());
		}
		query.append("gty",GoodsType.LESSON.getType());
		List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, Constant.FIELDS, new BasicDBObject("lut", Constant.DESC), skip, limit);
		if(null != list && !list.isEmpty()){
			for (DBObject dbo : list) {
				retList.add(new OrderEntry((BasicDBObject) dbo));
			}
		}
		return retList;
	}
	
	/**
	 * 根据条件查询销售订单个数
	 * @param lessonName
	 * @param startTime
	 * @param endTime
	 * @param nickName
	 * @param state
	 * @return
	 */
	public int count(ObjectId userId, String lessonName, long startTime, long endTime, String nickName, OrderState state){
		BasicDBObject query = new BasicDBObject();
		if(null != userId){
			query.append("oi.id", userId);
		}
		if(null != lessonName && lessonName.length() != 0){
			query.append("gi.v", lessonName);
		}
		if(0 != startTime){
			query.append("lut", new BasicDBObject(QueryOperators.GTE, startTime));
		}
		if(0 != endTime){
			query.append("lut", new BasicDBObject(QueryOperators.LTE, endTime));
		}
		if(null != nickName && nickName.length() != 0){
			query.append("ui.v", nickName);
		}
		if(null != state){
			query.append("st", state.getType());
		}
		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query);
	}
	
	/**
	 * 根据用户和商品ID查询订单 
	 * @param user
	 * @param goodsId
	 * @return
	 */
	public OrderEntry getOrderEntry(ObjectId user,ObjectId goodsId)
	{
		DBObject query=  new BasicDBObject("ui",user).append("gi", goodsId);
	    DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, Constant.FIELDS);
	    if(null!=dbo)
	    {
	    	return new OrderEntry((BasicDBObject)dbo);	
	    }
	    
	    return null;
	}
	
	/**
	 * 根据订单id查询订单
	 * @param orderId
	 * @return
	 */
	public OrderEntry getOrderEntryByOrderId(ObjectId orderId){
		DBObject query = new BasicDBObject(Constant.ID, orderId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, Constant.FIELDS);
		if(null != dbo){
			return new OrderEntry((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 * 根据订单编号查询订单
	 * @param orderId
	 * @return
	 */
	public OrderEntry getOrderEntryByOrderNum(String orderId){
		DBObject query = new BasicDBObject("onum", orderId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, Constant.FIELDS);
		if(null != dbo){
			return new OrderEntry((BasicDBObject) dbo);
		}
		return null;
	}
	
	
	/**
	 * 临时使用
	 * @param goodsId
	 * @return
	 */
	@Deprecated
	public List<OrderEntry> getOrderEntrys(ObjectId goodsId){
		List<OrderEntry> retList =new ArrayList<OrderEntry>();
		DBObject query = new BasicDBObject("gi.id", goodsId);
		List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, Constant.FIELDS);
		if(null != dbos && !dbos.isEmpty()){
			for(DBObject dbo:dbos)
			{
				retList.add( new OrderEntry((BasicDBObject) dbo));
			}
		}
		return retList;
	}
	
	
	
	
	
	
	private DBObject buildQuery(int sub, int gradeType, ObjectId user,
			GoodsType type, OrderState state, OrderType orderType) {
		BasicDBObject query =new BasicDBObject("ui.id",user);
		if(sub>Constant.NEGATIVE_ONE)
		{
			query.append("sub", sub);
		}
		if(gradeType>Constant.NEGATIVE_ONE)
		{
			query.append("ccgts", gradeType);
		}
		if(null!=type)
		{
			query.put("gty", type.getType());
		}
		if(null!=state)
		{
			query.put("st", state.getType());
		}
		if(null!=orderType)
		{
			query.put("ot", orderType.getStatus());
		}
		return query;
	}

	public void updateExpiretime(ObjectId id,long paytype, long expiretime) {
		DBObject query =new BasicDBObject(Constant.ID,id);
		BasicDBObject updatevalue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("et",expiretime).append("pat", paytype)
				.append("st", OrderState.COMPLETE.getType())
				.append("lut", new Date().getTime()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, updatevalue);
	}
	
	
	
	
	
}
