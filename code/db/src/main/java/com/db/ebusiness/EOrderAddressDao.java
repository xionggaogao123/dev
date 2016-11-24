package com.db.ebusiness;

import java.util.*;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EOrderAddressEntry;
import com.sys.constants.Constant;

/**
 * 订单地址操作类
 * @author fourer
 *
 */
public class EOrderAddressDao extends BaseDao {


	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addEOrderAddress(EOrderAddressEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERSADDRESS, e.getBaseEntry());
		return e.getID();
	}
	
	
	/**
	 * 得到用户地址
	 * @param ui
	 * @return
	 */
	public List<EOrderAddressEntry> getEOrderAddressEntrys(ObjectId ui)
	{
		List<EOrderAddressEntry> retList =new ArrayList<EOrderAddressEntry>();
		BasicDBObject query = new BasicDBObject("ui",ui);
		BasicDBObject sort = new BasicDBObject("def",-1).append(Constant.ID,-1);
		List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERSADDRESS,query, Constant.FIELDS,sort);
		if(null!=dbos && dbos.size()>0)
		{
			for(DBObject dbo:dbos)
			{
			  retList.add( new EOrderAddressEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}
	
	
	
	/**
	 * 得到地址详细
	 * @param
	 * @return
	 */
	public EOrderAddressEntry getEOrderAddressEntry(ObjectId id)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERSADDRESS,query, Constant.FIELDS);
		if(null!=dbo )
		{
			 return ( new EOrderAddressEntry((BasicDBObject)dbo));
		}
		return null;
	}


	public Map<ObjectId, EOrderAddressEntry> getEOrderAddressEntryMap(Collection<ObjectId> addressIds, DBObject fields){
		Map<ObjectId, EOrderAddressEntry> retMap = new HashMap<ObjectId, EOrderAddressEntry>();
		BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,addressIds));
		List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERSADDRESS,query, fields);
		if(null!=list && !list.isEmpty()) {
			EOrderAddressEntry e;
			for(DBObject dbo:list) {
				e = new EOrderAddressEntry((BasicDBObject)dbo);
				retMap.put(e.getID(), e);
			}
		}
		return retMap;
	}


	/**
	 * 删除地址
	 * */
	public void deleteAddress(ObjectId id){
		BasicDBObject query = new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_ORDERSADDRESS,query);
	}

	/**
	 * 修改地址
	 * */
	public ObjectId updateAddress(EOrderAddressEntry entry){
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERSADDRESS, entry.getBaseEntry());
		return entry.getID();
	}

	/**
	 * 设为默认地址
	 * */
	public void updateDefaultAddress(ObjectId userId,ObjectId addressId){
		//将原默认地址设为非默认
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_ORDERSADDRESS,new BasicDBObject("ui",userId),
				new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("def",0)));

		BasicDBObject query = new BasicDBObject(Constant.ID,addressId);
		BasicDBObject updateValue = new BasicDBObject("def",1);
		BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,updateValue);
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_ORDERSADDRESS,query,update);
	}

}
