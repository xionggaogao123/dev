package com.db.ebusiness;

import java.util.*;

import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import com.db.base.SynDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EGoodsEntry;
import com.sys.constants.Constant;


/**
 * 商品dao
 * @author fourer
 *
 */
public class EGoodsDao extends SynDao {

	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addEGoodsEntry(EGoodsEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS, e.getBaseEntry());
		return e.getID();
	}
	
	
	/**
	 * 详情
	 * @param eId
	 * @param fields
	 * @return
	 */
	public EGoodsEntry getEGoodsEntry(ObjectId eId,DBObject fields)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,eId);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS,query, fields);
		if(null!=dbo)
		{
			return new EGoodsEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	
	/**
	 * 根据ID查询，用于推荐商品
	 * @param eIds
	 * @param fields
	 * @return
	 */
	public List<EGoodsEntry> getEGoodsEntrys(Collection<ObjectId> eIds,DBObject fields)
	{
		List<EGoodsEntry> retList =new ArrayList<EGoodsEntry>();
		BasicDBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,eIds));
		List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS,query, fields);
		if(null!=dbos && dbos.size()>0)
		{
			for(DBObject dbo:dbos)
			{
			  retList.add( new EGoodsEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}
	
	
	
	
	/**
	 * 根据ID查询，用于推荐商品
	 * @param eIds
	 * @param fields
	 * @return
	 */
	public Map<ObjectId,EGoodsEntry> getEGoodsEntryMap(Collection<ObjectId> eIds,DBObject fields)
	{
		Map<ObjectId,EGoodsEntry> retMap =new HashMap<ObjectId, EGoodsEntry>();
		BasicDBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,eIds));
		List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS,query, fields);
		if(null!=dbos && dbos.size()>0)
		{
			EGoodsEntry e;
			for(DBObject dbo:dbos)
			{
				e=( new EGoodsEntry((BasicDBObject)dbo));
				retMap.put(e.getID(), e);
			}
		}
		return retMap;
	}
	
	
	
	
	

	/**
	 * 查询商品列表
	 * @param fields
	 * @param sort 排序方式
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<EGoodsEntry> getEGoodsEntrys(DBObject fields, DBObject sort, int state, int activity ,int groupPurchase, ObjectId goodsCategoryId,ObjectId levelGoodsCategoryId,
											 Set<ObjectId> gradeCategorySet,int bookCategory,int[] priceArr, String regular, int skip, int limit){
		List<EGoodsEntry> retList =new ArrayList<EGoodsEntry>();
		BasicDBObject query =new BasicDBObject("st",state);
		if(goodsCategoryId != null){
			query.append("gcl", goodsCategoryId);
		}
		if(levelGoodsCategoryId != null){
			query.append("lgcl", levelGoodsCategoryId);
		}
		if(gradeCategorySet.size() > 0){
			query.append("grcl", new BasicDBObject(Constant.MONGO_IN,MongoUtils.convert(gradeCategorySet)));
		}
		if(bookCategory != 0){
			query.append("bc",bookCategory);
		}
		if(activity !=-1){
			query.append("actvt",activity);
		}
		if(groupPurchase !=-1){
			query.append("gpc",groupPurchase);
		}
		if(priceArr != null){
			int minPrice = priceArr[0];
			int maxPrice = priceArr[1];
			query.append("pr",new BasicDBObject(Constant.MONGO_GTE,minPrice).append(Constant.MONGO_LTE,maxPrice));
		}

		if(null != regular && !regular.equals("")){
			String[] res = regular.split(" ");
			StringBuilder sb = new StringBuilder();
			sb.append("^");
			for(int i = 0;i < res.length; i++){
				sb.append("(?=.*?").append(res[i]).append(")");
			}
			sb.append(".+$");
			query.append("nm", MongoUtils.buildRegex(sb.toString()));
		}
		List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS, query, fields, sort, skip, limit);
		if(null!=dbos && dbos.size()>0)
		{
			for(DBObject dbo:dbos)
			{
				retList.add( new EGoodsEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}

	/**
	 * 查询商品总数
	 * @return
	 */
	public int getEGoodsEntrysCount(int state,int activity,int groupPurchase,ObjectId goodsCategoryId,ObjectId levelGoodsCategoryId, Set<ObjectId> gradeCategorySet,int bookCategory, int[] priceArr, String regular){
		BasicDBObject query =new BasicDBObject("st",state);
		if(goodsCategoryId != null){
			query.append("gcl", goodsCategoryId);
		}
		if(levelGoodsCategoryId != null){
			query.append("lgcl", levelGoodsCategoryId);
		}
		if(gradeCategorySet.size() > 0){
			query.append("grcl", new BasicDBObject(Constant.MONGO_IN,MongoUtils.convert(gradeCategorySet)));
		}
		if(bookCategory != 0){
			query.append("bc",bookCategory);
		}
		if(activity !=-1){
			query.append("actvt",activity);
		}
		if(groupPurchase !=-1){
			query.append("gpc",groupPurchase);
		}

		if(priceArr != null){
			int minPrice = priceArr[0];
			int maxPrice = priceArr[1];
			query.append("pr",new BasicDBObject(Constant.MONGO_GTE,minPrice).append(Constant.MONGO_LTE,maxPrice));
		}
		if(null != regular && !regular.equals("")){
			String[] res = regular.split(" ");
			StringBuilder sb = new StringBuilder();
			sb.append("^");
			for(int i = 0;i < res.length; i++){
				sb.append("(?=.*?").append(res[i]).append(")");
			}
			sb.append(".+$");
			query.append("nm", MongoUtils.buildRegex(sb.toString()));
		}
		int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS, query);
		return count;
	}
	
	
	/**
	 * 增加卖出数量
	 * @param eId
	 */
	public void increaseSellCount(ObjectId eId,int count)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,eId);
		BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("sc", count));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS,query, updateValue);
	}

	/**
	 * 更新评价个数
	 * @param goodsId
	 * @param field
	 * @param count
	 */
	public void updateCommentSummary(ObjectId goodsId, String field, int count){
		BasicDBObject query =new BasicDBObject(Constant.ID, goodsId).append("cs.nm", field);
		BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("cs.$.v", count));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS,query, updateValue);
	}

	/**
	 * 物理删除
	 * @param goodsId
	 */
	public void deleteEGoods(ObjectId goodsId){
		BasicDBObject query =new BasicDBObject(Constant.ID, goodsId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS,query);
	}

	@Deprecated
	public List<EGoodsEntry> findEGoods(int skip, int limit){
		List<EGoodsEntry> retList = new ArrayList<EGoodsEntry>();
		BasicDBObject query = new BasicDBObject();
		List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS,query,Constant.FIELDS,new BasicDBObject("_id", 1), skip, limit);
		if(null != dbObjectList){
			for(DBObject dbObject : dbObjectList){
				retList.add(new EGoodsEntry((BasicDBObject)dbObject));
			}
		}
		return retList;
	}
	
}
